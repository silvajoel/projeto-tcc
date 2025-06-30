package com.joelchagas.tcc.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.joelchagas.tcc.data.model.GlicemiaData
import com.joelchagas.tcc.data.model.InsulinaData
import com.joelchagas.tcc.data.model.Refeicao
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.data.model.TipoRefeicao
import helper.DatabaseConstants

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USUARIO)
        db?.execSQL(CREATE_TABLE_GLICEMIA)
        db?.execSQL(CREATE_TABLE_INSULINA)
        db?.execSQL(CREATE_TABLE_ALIMENTO)
        db?.execSQL(CREATE_TABLE_REFEICAO)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS ${DatabaseConstants.usuario.TABLE_NAME}"
        val dropGlicemiaQuery = "DROP TABLE IF EXISTS ${DatabaseConstants.glicemia.TABLE_NAME}"
        val dropInsulinaQuery = "DROP TABLE IF EXISTS ${DatabaseConstants.insulina.TABLE_NAME}"
        val dropAlimentoQuery = "DROP TABLE IF EXISTS ${DatabaseConstants.alimento.NOME_TABELA}"
        db?.execSQL(dropTableQuery)
        db?.execSQL(dropGlicemiaQuery)
        db?.execSQL(dropInsulinaQuery)
        db?.execSQL(dropAlimentoQuery)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "Database"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_USUARIO = """
            CREATE TABLE ${DatabaseConstants.usuario.TABLE_NAME} (
                ${DatabaseConstants.usuario.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseConstants.usuario.COLUMNS.NOME} TEXT NOT NULL,
                ${DatabaseConstants.usuario.COLUMNS.SOBRENOME} TEXT NOT NULL,
                ${DatabaseConstants.usuario.COLUMNS.DATA_NASC} TEXT NOT NULL,
                ${DatabaseConstants.usuario.COLUMNS.EMAIL} TEXT NOT NULL,
                ${DatabaseConstants.usuario.COLUMNS.SENHA} TEXT NOT NULL,
                ${DatabaseConstants.usuario.COLUMNS.TIPO} INTEGER DEFAULT 1
            );
        """

        private const val CREATE_TABLE_GLICEMIA = """
            CREATE TABLE ${DatabaseConstants.glicemia.TABLE_NAME} (
                ${DatabaseConstants.glicemia.GlicemiaColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseConstants.glicemia.GlicemiaColumns.USER_ID} INTEGER NOT NULL,
                ${DatabaseConstants.glicemia.GlicemiaColumns.NIVEL_GLICOSE} FLOAT NOT NULL,
                ${DatabaseConstants.glicemia.GlicemiaColumns.DATA_HORA_MEDICAO} TEXT NOT NULL,
                ${DatabaseConstants.glicemia.GlicemiaColumns.MOMENTO_MEDICAO} TEXT NOT NULL,
                ${DatabaseConstants.glicemia.GlicemiaColumns.OBSERVACOES} TEXT NOT NULL,
                FOREIGN KEY (${DatabaseConstants.glicemia.GlicemiaColumns.USER_ID}) REFERENCES ${DatabaseConstants.usuario.TABLE_NAME}(${DatabaseConstants.usuario.COLUMNS.ID})
            );
        """

        private const val CREATE_TABLE_INSULINA = """
            CREATE TABLE ${DatabaseConstants.insulina.TABLE_NAME} (
                ${DatabaseConstants.insulina.InsulinaColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseConstants.insulina.InsulinaColumns.USER_ID} INTEGER NOT NULL,
                ${DatabaseConstants.insulina.InsulinaColumns.INSULINA_QUANTIDADE} INTEGER NOT NULL,
                ${DatabaseConstants.insulina.InsulinaColumns.TIPO_INSULINA} TEXT NOT NULL,
                ${DatabaseConstants.insulina.InsulinaColumns.DATA_HORA_APLICACAO} TEXT NOT NULL,
                ${DatabaseConstants.insulina.InsulinaColumns.OBSERVACOES} TEXT NOT NULL,
                ${DatabaseConstants.insulina.InsulinaColumns.MOMENTO_MEDICAO} TEXT NOT NULL,
                FOREIGN KEY (${DatabaseConstants.insulina.InsulinaColumns.USER_ID}) REFERENCES ${DatabaseConstants.usuario.TABLE_NAME}(${DatabaseConstants.usuario.COLUMNS.ID})
            );
        """

        private const val CREATE_TABLE_ALIMENTO = """
            CREATE TABLE ${DatabaseConstants.alimento.NOME_TABELA} (
                ${DatabaseConstants.alimento.Colunas.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseConstants.alimento.Colunas.ID_USUARIO} INTEGER NOT NULL,
                ${DatabaseConstants.alimento.Colunas.ID_REFEICAO} INTEGER,
                ${DatabaseConstants.alimento.Colunas.DESCRICAO} TEXT NOT NULL,
                ${DatabaseConstants.alimento.Colunas.CATEGORIA} TEXT NOT NULL,
                ${DatabaseConstants.alimento.Colunas.QUANTIDADE} REAL NOT NULL,
                ${DatabaseConstants.alimento.Colunas.CALORIAS} REAL NOT NULL,
                ${DatabaseConstants.alimento.Colunas.PROTEINAS} REAL NOT NULL,
                ${DatabaseConstants.alimento.Colunas.GORDURAS} REAL NOT NULL,
                ${DatabaseConstants.alimento.Colunas.CARBOIDRATOS} REAL NOT NULL,
                FOREIGN KEY (${DatabaseConstants.alimento.Colunas.ID_USUARIO}) REFERENCES ${DatabaseConstants.usuario.TABLE_NAME}(${DatabaseConstants.usuario.COLUMNS.ID}),
                FOREIGN KEY (${DatabaseConstants.alimento.Colunas.ID_REFEICAO}) REFERENCES ${DatabaseConstants.refeicao.NOME_TABELA}(${DatabaseConstants.refeicao.Colunas.ID})
            );
        """
        private const val CREATE_TABLE_REFEICAO = """
            CREATE TABLE ${DatabaseConstants.refeicao.NOME_TABELA} (
                ${DatabaseConstants.refeicao.Colunas.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseConstants.refeicao.Colunas.USER_ID} INTEGER NOT NULL,
                ${DatabaseConstants.refeicao.Colunas.TIPO_REFEICAO} TEXT NOT NULL,
                ${DatabaseConstants.refeicao.Colunas.DATA_HORA} TEXT NOT NULL,
                FOREIGN KEY (${DatabaseConstants.refeicao.Colunas.USER_ID}) REFERENCES ${DatabaseConstants.usuario.TABLE_NAME}(${DatabaseConstants.usuario.COLUMNS.ID})
            );
        """
    }


    fun insertUsuario(
        nome: String,
        sobrenome: String,
        data_nasc: String,
        email: String,
        senha: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.usuario.COLUMNS.NOME, nome)
            put(DatabaseConstants.usuario.COLUMNS.SOBRENOME, sobrenome)
            put(DatabaseConstants.usuario.COLUMNS.DATA_NASC, data_nasc)
            put(DatabaseConstants.usuario.COLUMNS.EMAIL, email)
            put(DatabaseConstants.usuario.COLUMNS.SENHA, senha)
        }
        return db.insert(DatabaseConstants.usuario.TABLE_NAME, null, values)
    }

    fun insertGlicemia(
        nivel_glicose: String,
        data_hora_medicao: String,
        momento_medicao: String,
        observacoes: String
    ): Long {
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId == -1) return -1 // Nenhum usuário logado

        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.glicemia.GlicemiaColumns.USER_ID, userId)
            put(DatabaseConstants.glicemia.GlicemiaColumns.NIVEL_GLICOSE, nivel_glicose)
            put(DatabaseConstants.glicemia.GlicemiaColumns.DATA_HORA_MEDICAO, data_hora_medicao)
            put(DatabaseConstants.glicemia.GlicemiaColumns.MOMENTO_MEDICAO, momento_medicao)
            put(
                DatabaseConstants.glicemia.GlicemiaColumns.OBSERVACOES,
                if (observacoes.isEmpty()) "Sem observações" else observacoes
            )
        }

        return db.insert(DatabaseConstants.glicemia.TABLE_NAME, null, values)
    }

    fun insertInsulina(
        insulina_quantidade: String,
        tipo_insulina: String,
        data_hora_aplicacao: String,
        observacoes: String,
        momento_medicao: String
    ): Long {
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId == -1) return -1

        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.insulina.InsulinaColumns.USER_ID, userId)
            put(DatabaseConstants.insulina.InsulinaColumns.INSULINA_QUANTIDADE, insulina_quantidade)
            put(DatabaseConstants.insulina.InsulinaColumns.TIPO_INSULINA, tipo_insulina)
            put(DatabaseConstants.insulina.InsulinaColumns.DATA_HORA_APLICACAO, data_hora_aplicacao)
            put(
                DatabaseConstants.insulina.InsulinaColumns.OBSERVACOES,
                if (observacoes.isEmpty()) "Sem observações" else observacoes
            )
            put(DatabaseConstants.insulina.InsulinaColumns.MOMENTO_MEDICAO, momento_medicao)
        }

        return db.insert(DatabaseConstants.insulina.TABLE_NAME, null, values)
    }

    fun insertAlimento(
        descricao: String,
        categoria: String,
        quantidade_gramas: Double,
        calorias: Double,
        proteinas: Double,
        gorduras: Double,
        carboidratos: Double,
        idRefeicao: Int
    ): Long {
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)
        Log.d("InsertAlimento", "USER_ID recuperado: $userId")
        if (userId == -1) return -1

        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.alimento.Colunas.ID_USUARIO, userId)
            put(DatabaseConstants.alimento.Colunas.ID_REFEICAO, idRefeicao)
            put(DatabaseConstants.alimento.Colunas.DESCRICAO, descricao)
            put(DatabaseConstants.alimento.Colunas.CATEGORIA, categoria)
            put(DatabaseConstants.alimento.Colunas.QUANTIDADE, quantidade_gramas)
            put(DatabaseConstants.alimento.Colunas.CALORIAS, calorias)
            put(DatabaseConstants.alimento.Colunas.PROTEINAS, proteinas)
            put(DatabaseConstants.alimento.Colunas.GORDURAS, gorduras)
            put(DatabaseConstants.alimento.Colunas.CARBOIDRATOS, carboidratos)
        }

        return db.insert(DatabaseConstants.alimento.NOME_TABELA, null, values)
    }

    fun insertRefeicao(tipoRefeicao: String, dataHora: String): Long {
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId == -1) return -1

        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.refeicao.Colunas.USER_ID, userId)
            put(DatabaseConstants.refeicao.Colunas.TIPO_REFEICAO, tipoRefeicao)
            put(DatabaseConstants.refeicao.Colunas.DATA_HORA, dataHora)
        }

        return db.insert(DatabaseConstants.refeicao.NOME_TABELA, null, values)
    }

    fun getAllInsulinas(userId: Int): List<InsulinaData> {
        val insulinaList = mutableListOf<InsulinaData>()
        val db = readableDatabase
        val query =
            "SELECT * FROM ${DatabaseConstants.insulina.TABLE_NAME} WHERE ${DatabaseConstants.insulina.InsulinaColumns.USER_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.ID))
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.USER_ID))
                val quantidade =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.INSULINA_QUANTIDADE))
                val tipo =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.TIPO_INSULINA))
                val dataHora =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.DATA_HORA_APLICACAO))
                val observacoes =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.OBSERVACOES))
                val momento =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.insulina.InsulinaColumns.MOMENTO_MEDICAO))

                val insulina =
                    InsulinaData(id, userId, quantidade, tipo, dataHora, observacoes, momento)
                insulinaList.add(insulina)
            }
            cursor.close()
            db.close()
        }

        return insulinaList
    }

    fun getAllGlicemias(userId: Int): List<GlicemiaData> {
        val glicemiaList = mutableListOf<GlicemiaData>()
        val db = readableDatabase
        val query =
            "SELECT * FROM ${DatabaseConstants.glicemia.TABLE_NAME} WHERE ${DatabaseConstants.glicemia.GlicemiaColumns.USER_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            val id =
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.ID))
            val userIdFromDb =
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.USER_ID))
            val nivel_glicose =
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.NIVEL_GLICOSE))
            val data_hora_medicao =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.DATA_HORA_MEDICAO))
            val observacoes =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.OBSERVACOES))
            val momento_medicao =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.MOMENTO_MEDICAO))

            val glicemia = GlicemiaData(
                id,
                userIdFromDb,
                nivel_glicose,
                data_hora_medicao,
                observacoes,
                momento_medicao
            )
            glicemiaList.add(glicemia)
        }

        cursor.close()
        db.close()

        return glicemiaList
    }

    fun getAllAlimentos(userId: Int, tipoRefeicao: String): List<TacoAlimento> {
        val alimentoList = mutableListOf<TacoAlimento>()
        val db = readableDatabase

        val query = """
        SELECT 
            a.${DatabaseConstants.alimento.Colunas.ID} AS id,
            r.${DatabaseConstants.refeicao.Colunas.USER_ID} AS id_usuario,
            a.${DatabaseConstants.alimento.Colunas.DESCRICAO} AS descricao,
            a.${DatabaseConstants.alimento.Colunas.CATEGORIA} AS categoria,
            a.${DatabaseConstants.alimento.Colunas.QUANTIDADE} AS quantidade,
            a.${DatabaseConstants.alimento.Colunas.CALORIAS} AS calorias,
            a.${DatabaseConstants.alimento.Colunas.PROTEINAS} AS proteinas,
            a.${DatabaseConstants.alimento.Colunas.GORDURAS} AS gorduras,
            a.${DatabaseConstants.alimento.Colunas.CARBOIDRATOS} AS carboidratos
        FROM ${DatabaseConstants.alimento.NOME_TABELA} a
        INNER JOIN ${DatabaseConstants.refeicao.NOME_TABELA} r 
            ON a.${DatabaseConstants.alimento.Colunas.ID_REFEICAO} = r.${DatabaseConstants.refeicao.Colunas.ID}
        WHERE r.${DatabaseConstants.refeicao.Colunas.USER_ID} = ? 
          AND r.${DatabaseConstants.refeicao.Colunas.TIPO_REFEICAO} = ?
    """

        db.rawQuery(query, arrayOf(userId.toString(), tipoRefeicao)).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"))
                    val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
                    val categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"))
                    val quantidade = cursor.getDouble(cursor.getColumnIndexOrThrow("quantidade"))
                    val calorias = cursor.getDouble(cursor.getColumnIndexOrThrow("calorias"))
                    val proteinas = cursor.getDouble(cursor.getColumnIndexOrThrow("proteinas"))
                    val gorduras = cursor.getDouble(cursor.getColumnIndexOrThrow("gorduras"))
                    val carboidratos = cursor.getDouble(cursor.getColumnIndexOrThrow("carboidratos"))

                    val alimento = TacoAlimento(
                        id = id,
                        idUsuario = idUsuario,
                        descricao = descricao,
                        categoria = categoria,
                        quantidade = quantidade,
                        calorias = calorias,
                        proteinas = proteinas,
                        gorduras = gorduras,
                        carboidratos = carboidratos
                    )

                    alimentoList.add(alimento)
                } while (cursor.moveToNext())
            }
        }

        return alimentoList
    }

    fun deleteAlimento(id: Int): Int {
        val db = writableDatabase
        return db.delete(
            DatabaseConstants.alimento.NOME_TABELA,
            "${DatabaseConstants.alimento.Colunas.ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun updateAlimento(alimento: TacoAlimento): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DatabaseConstants.alimento.Colunas.DESCRICAO, alimento.descricao)
            put(DatabaseConstants.alimento.Colunas.CATEGORIA, alimento.categoria)
            put(DatabaseConstants.alimento.Colunas.QUANTIDADE, alimento.quantidade)
            put(DatabaseConstants.alimento.Colunas.CALORIAS, alimento.calorias)
            put(DatabaseConstants.alimento.Colunas.PROTEINAS, alimento.proteinas)
            put(DatabaseConstants.alimento.Colunas.GORDURAS, alimento.gorduras)
           put(DatabaseConstants.alimento.Colunas.CARBOIDRATOS, alimento.carboidratos)
        }
        return db.update(
            DatabaseConstants.alimento.NOME_TABELA,
            values,
            "${DatabaseConstants.alimento.Colunas.ID} = ?",
            arrayOf(alimento.id.toString())
        )
    }

    fun getUsuarioNome(email: String): String {
        val db = readableDatabase
        val selection = "${DatabaseConstants.usuario.COLUMNS.EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            DatabaseConstants.usuario.TABLE_NAME,
            arrayOf(DatabaseConstants.usuario.COLUMNS.NOME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var nome = ""
        if (cursor.moveToFirst()) {
            nome =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.usuario.COLUMNS.NOME))
        }
        cursor.close()
        return nome
    }

    fun getUsuarioId(email: String, senha: String): Int? {
        val db = readableDatabase
        val selection =
            "${DatabaseConstants.usuario.COLUMNS.EMAIL} = ? AND ${DatabaseConstants.usuario.COLUMNS.SENHA} = ?"
        val selectionArgs = arrayOf(email, senha)

        val cursor = db.query(
            DatabaseConstants.usuario.TABLE_NAME,
            arrayOf(DatabaseConstants.usuario.COLUMNS.ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.usuario.COLUMNS.ID))
        }
        cursor.close()
        return userId
    }

    fun getGlicemiaAtual(userId: Int): Int? {
        val db = readableDatabase

        val sql = """
        SELECT ${DatabaseConstants.glicemia.GlicemiaColumns.NIVEL_GLICOSE}
        FROM ${DatabaseConstants.glicemia.TABLE_NAME}
        WHERE ${DatabaseConstants.glicemia.GlicemiaColumns.USER_ID} = ?
        ORDER BY ${DatabaseConstants.glicemia.GlicemiaColumns.DATA_HORA_MEDICAO} DESC
        LIMIT 1
    """

        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))

        var glicemiaAtual: Int? = null
        if (cursor.moveToFirst()) {
            glicemiaAtual =
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstants.glicemia.GlicemiaColumns.NIVEL_GLICOSE))
        }
        cursor.close()
        return glicemiaAtual
    }

}
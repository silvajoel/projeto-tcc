package helper

class DatabaseConstants private constructor() {
    object usuario {
        const val TABLE_NAME = "usuario"

        object COLUMNS {
            const val ID = "id"
            const val NOME = "nome"
            const val SOBRENOME = "sobrenome"
            const val DATA_NASC = "data_nasc"
            const val EMAIL = "email"
            const val SENHA = "senha"
            const val TIPO = "tipo"
        }
    }

    object glicemia {
        const val TABLE_NAME = "glicemia"

        object GlicemiaColumns {
            const val ID = "id"
            const val USER_ID = "user_id"
            const val NIVEL_GLICOSE = "nivel_glicose"
            const val DATA_HORA_MEDICAO = "data_hora_medicao"
            const val MOMENTO_MEDICAO =
                "momento_medicao" // Ex: 'antes_refeicao', 'depois_refeicao', 'jejum', etc.
            const val OBSERVACOES = "observacoes"
        }
    }

    object insulina {
        const val TABLE_NAME = "insulina"

        object InsulinaColumns {
            const val ID = "id"
            const val USER_ID = "user_id"
            const val INSULINA_QUANTIDADE = "insulina_quantidade"
            const val TIPO_INSULINA = "tipo_insulina"
            const val DATA_HORA_APLICACAO = "data_hora_aplicacao"
            const val OBSERVACOES = "observacoes"
            const val MOMENTO_MEDICAO = "momento_medicao"

        }
    }

    object alimento {
        const val NOME_TABELA = "alimento"

        object Colunas {
            const val ID = "id"
            const val ID_USUARIO = "id_usuario"
            const val ID_REFEICAO = "id_refeicao"
            const val DESCRICAO = "descricao"
            const val CATEGORIA = "categoria"
            const val QUANTIDADE = "quantidade_gramas"
            const val CALORIAS = "calorias"
            const val PROTEINAS = "proteinas"
            const val GORDURAS = "gorduras"
            const val CARBOIDRATOS = "carboidratos"
        }
    }

    object refeicao {
        const val NOME_TABELA = "refeicao"

        object Colunas {
            const val ID = "id"
            const val USER_ID = "id_usuario"
            const val TIPO_REFEICAO = "tipo_refeicao"
            const val DATA_HORA= "data_hora"
        }
    }
}

    object WordCount {

        private fun normalize(str: String): String{
            val list = str.mapIndexed{ i,c ->
                when{
                    c.isLetterOrDigit() -> c
                    c == '\'' &&
                            ( (i-1)>0 && str[i-1].isLetterOrDigit() ) &&
                            ( (i+1)<str.length && str[i+1].isLetterOrDigit() ) -> c
                    else -> null
                }
            }.filterNotNull()
            return list.joinToString("")
        }

           fun phrase(phrase: String): Map<String, Int> {
               val wordsList = phrase.lowercase()
                   .split("""[^\w']+""".toRegex())
                   .filter{ !it.isNullOrEmpty() }
               return wordsList.map { normalize(it) }.groupingBy{it}.eachCount()
           }

    }



class Matrix(private val matrixAsString: String) {

    fun column(colNr: Int): List<Int> {
        val list: MutableList<Int> = mutableListOf()
        for(r in matrixAsString.split("\n")){
            var c = r.split(" ").elementAt(colNr-1).toInt()
            list.add(c)
        }
        return list
    }

    fun row(rowNr: Int): List<Int> {
        val r = matrixAsString.split("\n").elementAt(rowNr-1)
        return r.split(" ").map{it.toInt()}.toList()
    }

}

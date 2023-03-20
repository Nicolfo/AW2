object Flattener {
    fun recursive(source:Collection<Any?>,adder:(Any)->Unit){
        for(elem in source) {
            if (elem is Collection<Any?>)
                this.recursive(elem, adder);
            else if (elem != null)
                adder(elem);
        }
    }
    fun flatten(source: Collection<Any?>): List<Any> {
        val lista=mutableListOf<Any>();
        for(elem in source){
            if(elem is Collection<Any?>)
                this.recursive(elem){lista.add(it)}
            else if(elem!=null)
                lista.add(elem)
        }
        return lista;
    }
}

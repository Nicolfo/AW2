class Matrix(private val matrixAsString: String) {

    fun column(colNr: Int): List<Int> {
        var a=mutableListOf<Int>();
        var col =1;
        var s :String="";
        for (c in this.matrixAsString){
            if(c==' '){
                if(col==colNr)
                    a.add(s.toInt());
                s="";
                col+=1;
            }
            else if(c=='\n'){
                if(col==colNr)
                    a.add(s.toInt());
                col=1;
                s="";
            }
            else s+=c;

        }
        if (col==colNr)
            a.add(s.toInt());


        return a;
    }

    fun row(rowNr: Int): List<Int> {
        var a=mutableListOf<Int>();
        var row =1;
        var s="";
        for (c in this.matrixAsString){
            if(c=='\n'){
                if(row==rowNr)
                    a.add(s.toInt());
                row++;
                s="";
            }
            else if(c==' '){
                if(row==rowNr)
                    a.add(s.toInt());
                s="";
            }else{
                s+=c;
            }

        }
        if(row==rowNr)
            a.add(s.toInt());
        return a;
    }
}

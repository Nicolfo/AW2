object WordCount {

    fun phrase(phrase: String): Map<String, Int> {
        val phrase= "$phrase ";                                 //adding a separator at the end of the string just in case the string doesnt end with one

        val m=mutableMapOf<String,Int>();
        var s="";
        for(char in phrase){            //scan all the string character by character
            if(char.isLetter())
                s+=char.lowercase();    //if the scanned character is a letter -> lowercase() and add it to the current word
            if(char.isDigit())
                s+=char;                //if it is a digit add it without any operation
            if(s!="" && char=='\'' && s.last().isLetter())       //if a ' is seen it is removed if last isn't a letter(or the string is empty)
                    s+=char;
            if(char == ' '|| char== '\t' || char=='\n' || char==',') {       //if a separator is scanned we may be detecting a word
                if(s=="")                                       //if the string is empty multiple separator are find
                    continue;
                if(s.last()=='\'')                              //remove the ' if it is the last character -> 'word'
                    s=s.dropLast(1);
                if (m.contains(s)) {                            //if the Map already contains the word update the counter
                    var v = m.get(s);
                    if (v != null)
                        m.replace(s, v.toInt() + 1);
                    s = ""                                      //prepare the s for the next word
                } else {
                    m.put(s, 1);                                //if the word is seen for the first time add it with counter = 1
                    s = "";
                }

            }

        }
        return m;
    }
}

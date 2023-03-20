class Reactor<T>() {
    // Your compute cell's addCallback method must return an object
    // that implements the Subscription interface.

    interface Subscription {
        fun cancel()
    }

    inner abstract class Cell<T> (){
        abstract var value:T
        var riferimenti= mutableListOf<ComputeCell>()

    }

    inner class InputCell (value:T): Cell<T>(){
        override var value:T =value
            set(value){
                field=value
                riferimenti.forEach { it.updateValue() }                    //update all references values
                riferimenti.forEach { it.doCallBacks() }                //callbacks called after all updates
            }
    }

    inner class ComputeCell private constructor(val compute:()->T):Cell<T>(){
        override var value:T =compute()
        private val callBackList = mutableMapOf<Int,(T)->Any>();            //using Map instead of Vector because it's easier to remove/add values
        private var count=0;
        private var bufferCallBack=value;
        constructor (vararg references:Cell<T>,compute:(List<T>)->T) : this({ compute(references.map { it.value }) }){
            references.forEach { it.riferimenti.add(this) }
        }

        fun addCallback(f: (T) -> Any): Subscription{
            val callBackID=count
            count++
            callBackList.put(callBackID,f)

            return object : Subscription {
                override fun cancel() {
                    callBackList.remove(callBackID)
                }
            }
        }

        fun updateValue(){                                          //update the cell value if necessary
            val v2=compute();
            if(v2==this.value)
                return
            this.value=v2
            this.riferimenti.forEach { it.updateValue() }           //update all references
        }

        fun doCallBacks(){
            if(value==bufferCallBack){
                return
            }
            bufferCallBack=value;
            callBackList.forEach(){(_,y)->y(bufferCallBack)}    //do all callbacks linked to thos cell
            riferimenti.forEach { it.doCallBacks() }            //check if references has to do a callback
        }

    }


}

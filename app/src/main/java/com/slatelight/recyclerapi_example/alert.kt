package com.slatelight.recyclerapi_example

import android.content.Context
import android.widget.Toast

//  I did this cuz i don't wanna constantly write the long Toast line :)
fun alert(_ctx:Context, _text:String){
    Toast.makeText(_ctx,_text, Toast.LENGTH_SHORT).show()
}
fun alertL(_ctx:Context, _text:String){
    Toast.makeText(_ctx,_text, Toast.LENGTH_LONG).show()
}
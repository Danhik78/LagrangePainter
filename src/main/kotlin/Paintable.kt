import java.awt.Graphics

interface Paintable {
    var width: Int
    var height:Int
    var isVisible : Boolean
    fun paint(g : Graphics)
}
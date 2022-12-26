
import java.awt.BasicStroke
import java.awt.BasicStroke.CAP_ROUND
import java.awt.BasicStroke.JOIN_ROUND
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D

class GraphPainter(val convertor: Converter, var funct: (Double) -> Double, c : Color, override var isVisible:Boolean = true):Paintable{
    var color = c
    override var width: Int
        get() = convertor.width
        set(value) {convertor.width = value}
    override var height: Int
        get() = convertor.height
        set(value) {convertor.height=value}
    override fun paint(g: Graphics) {
        if(isVisible) {
            var x = 0
            (g as Graphics2D).setStroke(BasicStroke(3F, CAP_ROUND, JOIN_ROUND))
            g.color = color
            while (x <= width) {
                g.drawLine(
                    x,
                    convertor.yCrtToScr(funct(convertor.xScrToCrt(x))),
                    x + 1,
                    convertor.yCrtToScr(funct(convertor.xScrToCrt(x + 1)))
                )
                x += 1
            }
        }
    }

    fun ChangeFunct(f: (Double) -> Double)
    {
        funct = f
    }

    fun clear()
    {

    }


}
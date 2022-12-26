import ru.danila.math.polynomial.Newton
import java.awt.Color
import java.awt.Graphics

class DotPainter(override var width: Int, override var height: Int, val converter: Converter, val poly : Newton, ):Paintable {
    override var isVisible: Boolean = true
    var clr =Color(0,100,0)
    val xl = poly.xl
    val yl = poly.yl
    override fun paint(g: Graphics) {
        if (isVisible) {
            g.color = clr
            xl.forEachIndexed { i, v -> g.fillOval(converter.xCrtToScr(v) - 4, converter.yCrtToScr(yl[i]) - 4, 8, 8) }
        }
    }

    fun clear() {
        xl.clear()
        yl.clear()
    }
}
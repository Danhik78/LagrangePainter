import java.lang.Integer.max
import java.lang.Math.abs

class Converter(
    xMin : Double,
    xMax : Double,
    yMin : Double,
    yMax : Double,
    width : Int,
    height : Int
) {

    var width: Int = 1
        get() = field-1
        set(value) {
            field = max(1, value)
        }
    var height: Int = 1
        get() = field-1
        set(value) {
            field = max(1, value)
        }
    init {
        this.width=width
        this.height=height
    }
    private var xMin: Double = 0.0
    private var xMax: Double = 0.1
    var xEdges: Pair<Double, Double>
        get() = Pair(xMin, xMax)
        set(value) {
            if (value.first < value.second) {
                xMin = value.first
                xMax = value.second
            } else {
                xMin = value.second
                xMax = value.first
            }
            if (abs(xMax - xMin) < 0.1) {
                xMin -= 0.1
                xMax += 0.1
            }
        }
    private var yMin: Double = 0.0
    private var yMax: Double = 0.1
    var yEdges: Pair<Double, Double>
        get() = Pair(yMin, yMax)
        set(value) {
            if (value.first < value.second) {
                yMin = value.first
                yMax = value.second
            } else {
                yMin = value.second
                yMax = value.first
            }
            if (abs(yMax - yMin) < 0.1) {
                yMin -= 0.1
                yMax += 0.1
            }
        }
    val yDen
        get() = height / (yMax - yMin)

    init {
        xEdges = Pair(xMin, xMax)
    }

    val xDen
        get() = width / (xMax - xMin)

    init {
        xEdges = Pair(xMin, xMax)
    }

    fun xCrtToScr(x: Double): Int {//=((x-xMin)*xDen).coerceIn(-1.0*width,2.0*width).toInt()
        var res = ((x - xMin) * xDen).toInt()
        if (res < -width) res = -width
        if (res > 2 * width) res = 2 * width
        return ((x - xMin) * xDen).coerceIn(-1.0 * width, 2.0 * width).toInt()
    }

    fun yCrtToScr(y: Double) = ((yMax - y) * yDen).coerceIn(-1.0 * height, 2.0 * height).toInt()
    fun xScrToCrt(x:Int) = xMin+x/xDen
    fun yScrToCrt(y:Int)=yMax-y/yDen
}
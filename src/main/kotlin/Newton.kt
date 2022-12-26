package ru.danila.math.polynomial

class Newton() : Polynomial() {
    val xl = arrayListOf<Double>()
    val yl = arrayListOf<Double>()

    constructor(_map: MutableMap<Double, Double>) : this() {
        _map.forEach { (x, y) ->
            xl.add(x)
            yl.add(y)
        }
        val res = Polynomial(0.0)
        for (i in 0 until xl.size)
            res += CulcItyiF(i)
        _coeff.removeLast()
        _coeff.addAll(res.coeff)
    }

    constructor(other: Newton, x: Double, y: Double) : this() {
        other.xl.forEach { x ->
            xl.add(x)
        }
        other.yl.forEach { y ->
            yl.add(y)
        }
        _coeff.removeLast()
        _coeff.addAll(other.coeff)
        if (!xl.contains(x)) {
            xl.add(x)
            yl.add(y)
            this += CulcItyiF(xl.size - 1)
        }
    }

    private fun CulcItyiF(i: Int): Polynomial {
        val res: Polynomial
        var sum: Double = 0.0
        for (j in 0..i) {
            var product: Double = 1.0
            for (l in 0..i) if (l != j) product /= xl[j] - xl[l]
            sum += product * yl[j]
        }
        res = Polynomial(1.0)
        for (j in 0 until i) res *= Polynomial(-xl[j], 1.0)
        return res * sum
    }

    fun addNode(x: Double, y: Double) {
        xl.forEach { v -> if (Math.abs(v - x) < 0.05) return }
        xl.add(x)
        yl.add(y)
        this += CulcItyiF(xl.size - 1)
    }

    override fun clear()
    {
        xl.clear()
        yl.clear()
        _coeff.clear()
        _coeff.add(0.0)
    }
    fun deleteNode(x: Double, y: Double) {
        var _i: Int? = null
        xl.forEachIndexed { i, v -> if (Math.abs(v - x) < 1) _i = i }
        val j = _i
        if (j != null)
            if (Math.abs(yl[j] - y) < 1) {
                val __x = xl[j]
                val __y = yl[j]
                xl[j] = xl.last()
                yl[j] = yl.last()
                xl[xl.size - 1] = __x
                yl[yl.size - 1] = __y
                val res = Polynomial(*_coeff.toDoubleArray())
                if (xl.size != 0) res -= CulcItyiF(xl.size - 1)
                xl.removeLast()
                yl.removeLast()
                _coeff.clear()
                _coeff.addAll(res.coeff)
            }
    }
}
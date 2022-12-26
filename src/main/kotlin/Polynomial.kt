package ru.danila.math.polynomial

import java.text.DecimalFormat

open class Polynomial(vararg coeffs: Double) : Function<Double>{
    public val _coeff: MutableList<Double>
    val coeff: List<Double>
        get() = _coeff.toList()
    init{
        _coeff = coeffs.toMutableList()
        while(_coeff[_coeff.size-1]==0.0&&order>0) _coeff.removeLast()
    }
    constructor() :this(0.0)
    val order: Int
        get()=_coeff.size-1

    override fun toString(): String {
        val sb = StringBuilder()
        val df = DecimalFormat("#.###########")
         _coeff.asReversed().forEachIndexed { index, d ->
             if(order==0)return df.format(_coeff[0])
            if(_coeff[_coeff.size-1-index]+1.0 neq 1.0) {
                if (index > 0 && _coeff[_coeff.size-1-index]>0) sb.append("+")
                if(Math.abs(d) neq 1.0 || index==_coeff.size-1)
                        sb.append(df.format(d))
                else if (d les 0.0) sb.append('-')
                if(index != _coeff.size - 1) {
                    sb.append("x")
                    if (index != _coeff.size - 2) {
                        sb.append("^")
                        sb.append(_coeff.size - 1 - index)
                    }
                }
            }
        }
        return sb.toString()
    }

    operator fun plus(other: Polynomial): Polynomial{
        val (min,max) = if(order < other.order) arrayOf(coeff,other.coeff) else arrayOf(other.coeff,this.coeff)
        val res = max.toDoubleArray()
        min.forEachIndexed{i,v->res[i]+=min[i]}
        val _res = Polynomial(*res)
        return _res
    }
    operator fun times(mult: Double) = Polynomial(*DoubleArray(order+1){_coeff[it]*mult})
    /*val res = coeff.toDoubleArray()
        res.forEachIndexed{i,v -> res[i]*=mult}
        val _res = Polynomial(*res)
        return _res*/
    operator fun unaryMinus() = this*(-1.0)
    operator fun unaryPlus() = Polynomial(*_coeff.toDoubleArray())
    operator fun times(other: Polynomial) : Polynomial {
        val res = DoubleArray(order + other.order+1)
        _coeff.forEachIndexed{i,v->other._coeff.forEachIndexed{j,u->res[i+j]+= if(v eq 0.0||u eq 0.0) 0.0 else v*u}}
        val _res = Polynomial(*res)
        return _res
    }
    operator fun minus(other: Polynomial) = this + other*-1.0
    operator fun plusAssign(other: Polynomial)
    {
        val res = this + other
        this._coeff.clear()
        this._coeff.addAll(res.coeff)
        /*var i =0
        while(i<=other.order) {
            if (i <= this.order) _coeff[i] += other._coeff[i]
            else this._coeff.add(other._coeff[i])
            i++
        }
        while(Math.abs(_coeff[_coeff.size-1])<=Double.MIN_VALUE && order>0) _coeff.removeLast()*/
    }
    operator fun timesAssign(other: Polynomial)
    {
        val res = this * other
        this._coeff.clear()
        this._coeff.addAll(res.coeff)
    }
    operator fun timesAssign(other: Double)
    {
        _coeff.forEachIndexed{i,v -> _coeff[i]*=other}
    }
    operator fun divAssign(other: Double)
    {
        if(other+0.000001 eq 0.000001) throw Exception("Делить на ноль нельзя!")
        _coeff.forEachIndexed{i,v -> _coeff[i]/=other}
    }
    operator fun minusAssign(other: Polynomial) = this.plusAssign( other*-1.0)
    operator fun div(other: Double) :Polynomial {
        if(Math.abs(other)<=Double.MIN_VALUE) throw Exception("Делить на ноль нельзя!")
        return this * (1.0 / other)
    }

    override fun equals(other: Any?): Boolean {
        if(!(other is Polynomial))return false
        _coeff.forEachIndexed{i,v -> if(Math.abs(_coeff[i]-other._coeff[i])>Double.MIN_VALUE) return false}
        return true
    }
    //operator fun invoke(x : Double) = DoubleArray(order+1){coeff[it]*Math.pow(x,it.toDouble())}.sum()

    open operator fun invoke(x: Double): Double {
        var p = 1.0
        return _coeff.reduce { acc, d -> p *= x; acc + d * p; }
    }
    /*
    operator fun invoke(x: Double) =
        _coeff.reduceIndexed { index, acc, d -> acc + d * Math.pow(x, index.toDouble())  }
    */

    override fun hashCode() = _coeff.hashCode()

    constructor(lst : List<Double> ) : this(*lst.toDoubleArray())



    fun Derivitive():Polynomial
    {
        val res = coeff.toDoubleArray()
        res.forEachIndexed{i,v ->if(i<res.size-1) res[i]=res[i+1]*(i+1) else res[i]=0.0}
        return Polynomial(*res)
    }

    open fun clear() {
        _coeff.clear()
        _coeff.add(0.0)
    }
}
package ru.danila.math.polynomial

class Lagrange(val map : MutableMap<Double,Double>) : Polynomial() {
    init{
        val res = Polynomial(0.0)
        map.forEach{x,y-> res+=FundumentalPoly(x)*y}
        _coeff.removeLast()
        _coeff.addAll(res.coeff)
    }

    private fun FundumentalPoly(x: Double): Polynomial {
        val res = Polynomial(1.0)
        map.forEach{(_x)->
            if(_x!=x) res *=Polynomial(-_x,1.0)/(x-_x)
        }
        return res
    }
}
/*
* var a:Int?=2
* fun method(){
*   if(a!=null){
*       val b = a+2
*   }
* }
* */

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.text.DecimalFormat


class CortesianPainter(override var width: Int, override var height: Int,val convertor: Converter) :Paintable {
    var clr = Color.BLACK
    override var isVisible: Boolean = true
    override fun paint(g : Graphics) {
        if(isVisible) {
            g.color = clr
            var x = convertor.xCrtToScr(0.0).coerceIn(1, convertor.width)
            var y = convertor.yCrtToScr(0.0).coerceIn(1, convertor.height)
            (g as Graphics2D).setStroke(BasicStroke(2F))
            g.drawLine(0, y, convertor.width, y)
            g.drawLine(x, 0, x, convertor.height)
            //val d_x=0.1
            //val d_y=0.1
            val d_x = getD(convertor.xEdges.first,convertor.xEdges.second,convertor.width)
            val d_y = getD(convertor.yEdges.first,convertor.yEdges.second,convertor.height)
            drawSetka(x,y,g,d_x,d_y)
            drawPolosochki(x, y, g,d_x,d_y)
            if (x == 1 || x == convertor.width || y == 1 || y == convertor.height) {
                if (x == 1) x = convertor.width
                else if (x == convertor.width) x = 1
                if (y == 1) y = convertor.height
                else if (y == convertor.height) y = 1
                (g as Graphics2D).setStroke(BasicStroke(2F))
                g.drawLine(0, y, convertor.width, y)
                g.drawLine(x, 0, x, convertor.height)
                drawPolosochki(x, y, g,d_x,d_y)
            }
        }
    }

    private fun getD(first: Double, second: Double,length:Int): Double {
        var d_y=0.1
        var ten = 0.1
        while((second-first)/d_y > length/10)
        {
            if(d_y>ten*100.0)ten*=10
            d_y+=ten

        }
        ten = 1.0
        while((second-first)/d_y<length/30)
        {
            while(d_y/2<=ten) ten/=10
            d_y-=ten
        }
        return d_y
    }

    private fun drawSetka(x: Int, y: Int, g: Graphics2D,_d_x:Double,_d_y:Double) {
        var tick : Int = 0
        val d_y=_d_y
        val d_x = _d_x //растояние в СК
        var ten=0.1
        var xScr : Int = 0
        var yScr : Int =0
        val df = DecimalFormat("#.##")

        var _x = 0.0
        g.setStroke(BasicStroke(1F))
        g.color = Color(225, 225, 225)
        while(_x<convertor.xEdges.second)
        {



            _x+=d_x
            tick+=1
            xScr = convertor.xCrtToScr(_x)
            if(_x>=convertor.xEdges.first)
                if(tick%10 ==0)
                {
                    g.drawLine(xScr, 0, xScr, convertor.height)
                }

        }
        tick=0
        _x=0.0
        while(_x>convertor.xEdges.first)
        {

            _x-=d_x
            tick+=1
            xScr = convertor.xCrtToScr(_x)
            if(_x<=convertor.xEdges.second)
                if(tick%10 ==0)
                {
                    g.drawLine(xScr, 0, xScr, convertor.height)
                }

        }
        tick=0
        var _y = 0.0
        while(_y<convertor.yEdges.second)
        {
            _y+=d_y
            tick+=1
            yScr = convertor.yCrtToScr(_y)
            if(_y>=convertor.yEdges.first)
                if(tick%10 ==0)
                {
                    g.drawLine(0,yScr,convertor.width,yScr)
                }

        }
        tick=0
        _y=0.0
        while(_y>convertor.yEdges.first)
        {
            _y-=d_y
            tick+=1
            yScr = convertor.yCrtToScr(_y)
            if(_y<=convertor.yEdges.second)
                if(tick%10 ==0)
                {
                    g.drawLine(0,yScr,convertor.width,yScr)
                }
        }
    }

    private fun drawPolosochki(x : Int, y : Int,g: Graphics2D,_d_x: Double,_d_y: Double) {
        var tick : Int = 0
        var d_y=_d_y
        var d_x = _d_x //растояние в СК
        var xScr : Int = 0
        var yScr : Int =0
        val df = DecimalFormat("#.##")
        g.color=Color.BLACK
        var _x =0.0
        while(_x<convertor.xEdges.second)
        {


            g.setStroke(BasicStroke(1F))
            _x+=d_x
            tick+=1
            xScr = convertor.xCrtToScr(_x)
            if(_x>=convertor.xEdges.first)
            if(tick%10 ==0)
            {
                g.setStroke(BasicStroke(2F))
                g.drawLine(xScr,y-4,xScr,y+4)
                g.drawString(""+((tick/10*d_x*10+_x)/2).toFloat().toString(),xScr-2,y+15)
                g.setStroke(BasicStroke(1F))
            }else if(tick%5==0)
            {
                g.drawLine(xScr,y-4,xScr,y+4)
            }else g.drawLine(xScr,y-3,xScr,y+2)
        }
        tick=0
        _x=0.0
        while(_x>convertor.xEdges.first)
        {

            g.setStroke(BasicStroke(1F))
            _x-=d_x
            tick+=1
            xScr = convertor.xCrtToScr(_x)
            if(_x<=convertor.xEdges.second)
            if(tick%10 ==0)
            {
                g.setStroke(BasicStroke(2F))
                g.drawLine(xScr,y-4,xScr,y+4)
                g.drawString(""+((-tick/10*d_x*10+_x)/2).toFloat().toString(),xScr-2,y+15)
                g.setStroke(BasicStroke(1F))
            }else if(tick%5==0)
            {
                g.drawLine(xScr,y-4,xScr,y+4)
            }else g.drawLine(xScr,y-3,xScr,y+2)

        }
        tick=0
        var _y = 0.0
        while(_y<convertor.yEdges.second)
        {

            g.setStroke(BasicStroke(1F))
            _y+=d_y
            tick+=1
            yScr = convertor.yCrtToScr(_y)
            if(_y>=convertor.yEdges.first)
            if(tick%10 ==0)
            {
                g.setStroke(BasicStroke(2F))
                g.drawLine(x-4,yScr,x+4,yScr)
                g.drawString(""+((tick/10*d_y*10+_y)/2).toFloat().toString(),x+10,yScr+4)
                g.setStroke(BasicStroke(1F))
            }else if(tick%5==0)
            {
                g.drawLine(x-4,yScr,x+4,yScr)
            }else g.drawLine(x-3,yScr,x+2,yScr)

        }
        tick=0
        _y=0.0
        while(_y>convertor.yEdges.first)
        {

            g.setStroke(BasicStroke(1F))
            _y-=d_y
            tick+=1
            yScr = convertor.yCrtToScr(_y)
            if(_y<=convertor.yEdges.second)
            if(tick%10 ==0)
            {
                g.setStroke(BasicStroke(2F))
                g.drawLine(x-4,yScr,x+4,yScr)
                g.drawString(""+((-tick/10*d_y*10+_y)/2).toFloat().toString(),x+10,yScr+4)
                g.setStroke(BasicStroke(1F))
            }else if(tick%5==0)
            {
                g.drawLine(x-4,yScr,x+4,yScr)
            }else g.drawLine(x-3,yScr,x+2,yScr)

        }
    }


}
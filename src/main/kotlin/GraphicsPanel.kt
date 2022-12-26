
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class GraphicsPanel() : JPanel(){

    var img:BufferedImage?=null
    private var painterList: MutableList<Paintable> = mutableListOf()

    init {
    }

    fun addPaintable(painter: Paintable)
    {
        painterList.add(painter)
    }
    override fun paint(g: Graphics?,) {
        img = BufferedImage(this.width, this.height, 1)
        val ig = img?.graphics
        if (ig != null) {
            ig.color = Color.white
            ig.fillRect(0,0,width,height)
                painterList.forEach { it.paint(ig) }
            this.graphics.drawImage(img,0,0,null)
        }
        else
        {
            if(g!=null) {
                super.paint(g)
                painterList.forEach { it.paint(g) }
            }
        }
        img=null
        System.gc();
    }
    }

import ru.danila.math.polynomial.Newton
import ru.danila.math.polynomial.Polynomial
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import javax.swing.*


class KrutoeOkoshko:JFrame() {
    val minSize = Dimension(800,600)

    companion object{
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }
    val conv = Converter(-5.0, 5.0, -5.0, 5.0, 564, 434)
    val polyNew = Newton()
    var derNew = Polynomial()

    val baseGraphClr =Color(255,0,0)
    val baseDerClr=Color(190,190,255)
    
    val axesPainter = CortesianPainter(0,0,conv)
    val graphPainter = GraphPainter(conv,polyNew::invoke,baseGraphClr)
    val derivitivePainter = GraphPainter(conv,derNew::invoke,baseDerClr,false)
    val dotPainter = DotPainter(0,0,conv,polyNew)

   var mouseStart:Point? = null
    var wheelMoving :Boolean = false

    val panelDraw = GraphicsPanel().apply {
        background= Color.white
        addPaintable(axesPainter)
        addPaintable(derivitivePainter)
        addPaintable(graphPainter)
        addPaintable(dotPainter)
        addMouseWheelListener(object :MouseAdapter(){
            override fun mouseWheelMoved(e: MouseWheelEvent?) {
                if(e!=null)
                {
                    wheelMoving=true
                    val x =conv.xScrToCrt(e.x)
                    val y =conv.yScrToCrt(e.y)
                    val d =e.preciseWheelRotation/(-10)
                    nmXMin.value =Math.max(-1000.0,nmXMin.value as Double+(x - nmXMin.value as Double)*d)
                    nmXMax.value =Math.min(1000.0,nmXMax.value as Double +(x-nmXMax.value as Double)*d)
                    nmYMin.value =Math.max(-1000.0,nmYMin.value as Double+(y - nmYMin.value as Double)*d)
                    nmYMax.value =Math.min(1000.0,nmYMax.value as Double +(y-nmYMax.value as Double)*d)
                    wheelMoving= false
                    this@apply.paint(this@apply.graphics)
                }
            }
        })
        addMouseMotionListener(object :MouseAdapter(){
            override fun mouseDragged(e: MouseEvent?) {
                if(e!=null && mouseStart!=null){
                        val x = mouseStart?.x
                        val y = mouseStart?.y
                        if (x != null && y != null) {
                            val dx = conv.xScrToCrt(e.x) - conv.xScrToCrt(x)
                            val dy = conv.yScrToCrt(e.y) - conv.yScrToCrt(y)
                            if(nmXMax.value!=1000.0) {
                                nmXMin.value = Math.max(-1000.0, (nmXMin.value as Double) - dx)
                            }
                            if(nmXMin.value!=-1000.0){
                                nmXMax.value = Math.min(1000.0, (nmXMax.value as Double) - dx)
                            }
                            if(nmYMax.value!=1000.0) {
                                nmYMin.value = Math.max(-1000.0, (nmYMin.value as Double) - dy)
                            }
                            if(nmYMin.value!=-1000.0) {
                                nmYMax.value = Math.min(1000.0, (nmYMax.value as Double) - dy)
                            }
                            mouseStart = e.point
                            this@apply.paint(this@apply.graphics)
                        }
                }
            }

        })
        addMouseListener(object : MouseAdapter() {

            override fun mousePressed(e: MouseEvent?) {
                if(e!=null) mouseStart=e.point
            }
            override fun mouseReleased(e: MouseEvent?) {
                if(e!=null)mouseStart=null
            }


            override fun mouseClicked(e: MouseEvent?){
                if(e!=null){
                    if(e.button== MouseEvent.BUTTON1)
                        if(e.isShiftDown != true)
                polyNew.addNode(conv.xScrToCrt(e.x),conv.yScrToCrt(e.y))
                    if(e.button==MouseEvent.BUTTON3)
                        polyNew.deleteNode(conv.xScrToCrt(e.x),conv.yScrToCrt(e.y))
                    derNew=polyNew.Derivitive()
                    derivitivePainter.funct={x:Double->derNew.invoke(x)}
                    this@apply.paint(this@apply.graphics)
                }
            }


        })
    }

    val btnClear = JButton().apply {
        this.text="Purify"
        addActionListener{
            polyNew.clear()
            dotPainter.clear()
            derNew.clear()
            panelDraw.paint(panelDraw.graphics)
        }
    }

    val checkDot = JCheckBox().apply {
        isSelected = true
        addActionListener { dotPainter.isVisible=isSelected
            panelDraw.paint(panelDraw.graphics)
        }

    }
    val checkGraph= JCheckBox().apply {
        isSelected = true
        addActionListener { graphPainter.isVisible=isSelected
            panelDraw.paint(panelDraw.graphics)
        }
    }
    val checkDer = JCheckBox().apply {
        isSelected = false
        addActionListener { derivitivePainter.isVisible=isSelected
            panelDraw.paint(panelDraw.graphics)}
    }
    val panelChoose = JPanel().apply { background= Color(215,215,215) }
    val nmXMin = SpinnerNumberModel(-5.0,-100.0,4.8,0.1)
    val nmXMax = SpinnerNumberModel(5.0,-4.8,100.0,0.1)
    val nmYMin = SpinnerNumberModel(-5.0,-100.0,4.8,0.1)
    val nmYMax = SpinnerNumberModel(5.0,-4.8,100.0,0.1)
    val numericXmin = JSpinner(nmXMin).apply {
        addChangeListener { _->nmXMax.minimum = nmXMin.value as Double + nmXMin.stepSize.toDouble()*2.0
        conv.xEdges= Pair(nmXMin.value as Double,nmXMax.value as Double)
            if(mouseStart==null && !wheelMoving)panelDraw.paint(panelDraw.graphics)
        }
    }
    val numerisXmax = JSpinner(nmXMax).apply {
        addChangeListener { _->nmXMin.maximum = nmXMax.value as Double - nmXMin.stepSize.toDouble()*2.0
            conv.xEdges=Pair(nmXMin.value as Double,nmXMax.value as Double)
            if(mouseStart==null && !wheelMoving)panelDraw.paint(panelDraw.graphics)
        }
    }
    val numericYmin = JSpinner(nmYMin).apply {
        addChangeListener {
            nmYMax.minimum=nmYMin.value as Double + nmYMin.stepSize.toDouble()*2.0

            conv.yEdges=Pair(nmYMin.value as Double,nmYMax.value as Double)
            if(mouseStart==null && !wheelMoving)panelDraw.paint(panelDraw.graphics)
        }
    }
    val numericYmax = JSpinner(nmYMax).apply {
        addChangeListener { _->nmYMin.maximum = nmYMax.value as Double - nmYMin.stepSize.toDouble()*2.0
            conv.yEdges=Pair(nmYMin.value as Double,nmYMax.value as Double)
            if(mouseStart==null && !wheelMoving)panelDraw.paint(panelDraw.graphics)
        }
    }
    val labelXmin = JLabel("min X")
    val labelXmax = JLabel("max X")
    val labelYmin = JLabel("min Y")
    val labelYmax = JLabel("max Y")
    val labelDerColor = JLabel("Цвет производной")
    val labelDotColor = JLabel("Цвет точек")
    val labelGraphColor = JLabel("Цвет графика")
    val DerColor = JPanel().apply {
        background=baseDerClr
        addMouseListener(object : MouseAdapter(){
        override fun mouseClicked(e: MouseEvent?) {
            JColorChooser.showDialog(this@KrutoeOkoshko,"Выберите цвет производной",background)?.let{
                background = it
                derivitivePainter.color=it
                panelDraw.paint(panelDraw.graphics)
            }
        }
    })}
    val DotColor = JPanel().apply { background=Color(0,100,0)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                JColorChooser.showDialog(this@KrutoeOkoshko, "Выберите цвет производной", background)?.let {
                    background = it
                    dotPainter.clr = it
                    panelDraw.paint(panelDraw.graphics)
                }
            }
        }
        )
    }
    val GraphColor=JPanel().apply { background=baseGraphClr
        addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                JColorChooser.showDialog(this@KrutoeOkoshko,"Выберите цвет осей",background)?.let{
                    background = it
                    graphPainter.color=it
                    panelDraw.paint(panelDraw.graphics)
                }
            }
        })}

    init {
        minimumSize = minSize
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val gl = GroupLayout(this.contentPane)
        val glChoser = GroupLayout(panelChoose)
        layout = gl
        panelChoose.layout=glChoser
        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(8)
            .addComponent(panelDraw,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
            .addGap(8)
            .addComponent(panelChoose,100,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            .addGap(8)
        )
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGap(8)
            .addGroup(gl.createParallelGroup()
                .addComponent(panelDraw)
                .addComponent(panelChoose)
            )
            .addGap(8)
        )
        val nh = 20
        glChoser.setVerticalGroup(glChoser.createParallelGroup()
            .addGroup(glChoser.createSequentialGroup()
                .addGap(8,8,Int.MAX_VALUE)
                .addComponent(btnClear)
                .addGap(8,8,Int.MAX_VALUE))
            .addGroup(glChoser.createSequentialGroup()
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelXmin)
                    .addComponent(numericXmin,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelXmax)
                    .addComponent(numerisXmax,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
            )

            .addGroup(glChoser.createSequentialGroup()
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelYmin)
                    .addComponent(numericYmin,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelYmax)
                    .addComponent(numericYmax,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
            )
            .addGroup(glChoser.createSequentialGroup()
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(checkGraph)
                    .addComponent(GraphColor,20,20,20)
                    .addComponent(labelGraphColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(checkDer)
                    .addComponent(DerColor,20,20,20)
                    .addComponent(labelDerColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
                .addGap(8,8,Int.MAX_VALUE)
                .addGroup(glChoser.createParallelGroup()
                    .addComponent(checkDot)
                    .addComponent(DotColor,20,20,20)
                    .addComponent(labelDotColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8,8,Int.MAX_VALUE)
            )
        )
        val nw = 50
        glChoser.setHorizontalGroup(glChoser.createSequentialGroup()
            .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelXmin)
                    .addComponent(labelXmax)
                )
            .addGroup(glChoser.createParallelGroup()
                .addComponent(numericXmin,nw,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(numerisXmax,nw,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            )
            .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelYmin)
                    .addComponent(labelYmax)
            )
            .addGroup(glChoser.createParallelGroup()
                .addComponent(numericYmin,nw,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(numericYmax,nw,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            ).addGap(8,8,Int.MAX_VALUE)
            .addComponent(btnClear)
            .addGap(8,8,Int.MAX_VALUE)
            .addGroup(glChoser.createParallelGroup()
                .addComponent(checkGraph)
                .addComponent(checkDer)
                .addComponent(checkDot)
            )
            .addGap(8,8,8)
            .addGroup(glChoser.createParallelGroup()
                    .addComponent(GraphColor,20,20,20)
                    .addComponent(DerColor,20,20,20)
                    .addComponent(DotColor,20,20,20)
            )
            .addGroup(glChoser.createParallelGroup()
                    .addComponent(labelGraphColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDerColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDotColor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            )
            .addGap(8)
        )
    }


    override fun paint(g: Graphics?) {
        super.paint(g)
        conv.xEdges=Pair(numericXmin.value as Double,numerisXmax.value as Double)
        conv.yEdges= Pair(numericYmin.value as Double, numericYmax.value as Double)
        conv.height=panelDraw.height
        conv.width=panelDraw.width
        panelDraw.paint(panelDraw.graphics)
    }

}
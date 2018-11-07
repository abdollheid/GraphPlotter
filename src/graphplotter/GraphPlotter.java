/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphplotter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author abdo
 */
public class GraphPlotter extends Application {
    private final double  charHeight = 7 , charWidth = 7 ; 
    private double width = 800, height  = 800 , borderOffset = 30  , axisNumberLen = 5 , scale = 1 , numberXTextHeightOffset = 15 ;
    private double numberYTextHeightOffset  = 10 , axisEndOffset = 15  , senistifty = 100 , equationHeight = 100;  
    private int axisSidePartsX = 5 , axisSidePartsY = 5; 
   private ArrayList<Node> nodes  ;
   private ArrayList<String> eqs ; 
   public final Color COLORS [] = {Color.BLUE , Color.RED , Color.ORANGE , Color.GREEN , Color.YELLOW , Color.MEDIUMSLATEBLUE};
   
    
   
   
   @Override
    public void start(Stage primaryStage) throws Exception{
        
                
        
        
        Pane root = new Pane();
        
        
        
        
        drawLayout(root) ; 
        
        drawButtons(root) ; 
        
        Scene scene = new Scene(root, width , height + equationHeight);
        
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("graph plotter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public void drawLayout(Pane root ){
        nodes = new ArrayList<>() ; 
        drawAxis(root)  ;
        drawXAxisNumbers(root) ; 
        drawYAxisNumbers(root) ; 
    }
    public void drawAxis(Pane pane){
        Line axisX = new Line(0 + borderOffset , height/2 , width - borderOffset , height/2) ; 
        Line axisY = new Line(width/2 , 0 + borderOffset , width/2 , height - borderOffset) ; 
        
        pane.getChildren().addAll(axisX , axisY) ; 
        nodes.add(axisX); 
        nodes.add(axisY) ; 
        
        NodeSpeedUp(axisX);
        NodeSpeedUp(axisY);
    }
    
    
    
    public void drawXAxisNumbers(Pane pane){
        double xSideLen = (width - 2 * borderOffset - 2 * axisEndOffset) / 2.0  ; 
        for(int i = 1 ; i <= axisSidePartsX ; ++i){
            double lineXPos =  ( (i / (double)axisSidePartsX ) * xSideLen)  ; 
            Line lR= new Line((width/2) +lineXPos, height/2 ,(width/2)  +  lineXPos , height/2 - axisNumberLen) ;
            String numberR = (scale * i) + "" ; 
            Text tR  = new Text((width/2)  +lineXPos - numberWidth(numberR)/2, height/2  + numberXTextHeightOffset , numberR ) ;
           
            String numberL = (-scale * i) + "" ; 
            Line lL = new Line((width/2)  - lineXPos, height/2 , (width/2)  - lineXPos , height/2 - axisNumberLen) ;
            
            Text tL  = new Text((width/2) -lineXPos - numberWidth(numberL)/2, height/2  +numberXTextHeightOffset , numberL) ;
            
            
            pane.getChildren().addAll(lR , lL , tR , tL) ;
            nodes.add(lR) ; 
            nodes.add(lL) ; 
            nodes.add(tR) ; 
            nodes.add(tL) ; 
            
            NodeSpeedUp(lR);
            NodeSpeedUp(lL);
            NodeSpeedUp(tR);
            NodeSpeedUp(tL);
        }
    }
    
    public void drawYAxisNumbers(Pane pane){
        double ySideLen = (height - 2 * borderOffset - 2*axisEndOffset) / 2.0  ; 
        for(int i = 1 ; i <= axisSidePartsY ; ++i){
            double lineYPos =  ( (i / (double)axisSidePartsY ) * ySideLen)  ; 
            Line lT= new Line((width/2), height/2 + lineYPos , width/2 + axisNumberLen, height/2 + lineYPos ) ;
            String numberT = (-scale * i) + "" ; 
            Text tT  = new Text((width/2) + numberYTextHeightOffset  , height/2 + lineYPos + charHeight / 2  , numberT ) ;
           
            String numberL = (scale * i) + "" ; 
            Line lD= new Line((width/2) , height/2 - lineYPos , width/2 +axisNumberLen, height/2 - lineYPos ) ;
            Text tL  = new Text((width/2) + numberYTextHeightOffset  , height/2 - lineYPos + charHeight / 2  , numberL ) ;
//          
            
            
            pane.getChildren().addAll(lT ,lD , tT ,tL) ;
            nodes.add(lT) ;
            nodes.add(lD) ;
            nodes.add(tT) ;
            nodes.add(tL) ;
            NodeSpeedUp(lT);
            NodeSpeedUp(lD);
            NodeSpeedUp(tT);
            NodeSpeedUp(tL);
            
        }
    }
    
    public void clearAll(Pane root){
        for(Node n : nodes){
            root.getChildren().remove(n) ; 
        }
    }
    
    
    public void drawFunction(Pane pane , String func , int index)throws Exception{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
 
        
        
        
        Polyline polyline = new Polyline();
        ObservableList<Double> list = polyline.getPoints();
        
        final double centerX = width/2 , centerY = height/2 ; 
        
        

        double dividedPartXLen = (width/2 - borderOffset - axisEndOffset)  / axisSidePartsX; 
        double dividedPartYLen = (height/2 - borderOffset - axisEndOffset) / axisSidePartsY ; 
       
        double nextPoint = axisSidePartsX * 2 / senistifty ; 
     
        
        for(double i = -axisSidePartsX  ; i <= axisSidePartsX;  i += nextPoint ){
//            System.out.println("i:" + i); 
            double x = i * scale  ; 
            String tmp = func.replaceAll("x" , x + "") ; 
            tmp = tmp.replaceAll("X" , x + "") ; 
            double y =  (double) engine.eval(tmp) ; 
            
            
//            System.out.println("x:" + x);
//            System.out.println("y:" + y);
//            System.out.println("pw" + dividedPartXLen) ; 
//            System.out.println("ph" + dividedPartYLen) ; 
//            System.out.println("final:" + (centerX + x/scale * dividedPartXLen )) ; 
//            System.out.println("final:" +(centerY - (y/scale * dividedPartYLen))  ) ; 

            list.add(x/scale  * dividedPartXLen  + centerX );
            list.add(centerY - (y/scale * dividedPartYLen));
        }
        
        
        polyline.setStroke(COLORS[index%COLORS.length]);
        pane.getChildren().add(polyline) ; 
        nodes.add(polyline);

        NodeSpeedUp(polyline);

    }
    
    public void drawButtons(Pane root) {
        double btWidth = 30 , btHeight =30 ; 
        Button zoomIn = new Button("+") ; 
        zoomIn.setLayoutX(borderOffset);
        zoomIn.setMinSize(btWidth , btHeight);
        zoomIn.setMaxSize(btWidth , btHeight);
        zoomIn.setLayoutY(height + equationHeight - 2 * btHeight - borderOffset - 7);
        
        
        zoomIn.setOnAction(e->{
            clearAll(root) ; 
            scale /= 2  ; 
            drawLayout(root) ; 
            
            try {
                for(int i = 0 ; i < eqs.size() ; ++i)
                    drawFunction(root , eqs.get(i) , i) ;
            } catch (Exception ex) {
                Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        
        Button zoomOut = new Button("-") ; 
        zoomOut.setLayoutX(borderOffset);
        zoomOut.setMinSize(btWidth , btHeight);
        zoomOut.setMaxSize(btWidth , btHeight);
        zoomOut.setLayoutY(height+ equationHeight  - btHeight- borderOffset );
        
        
        zoomOut.setOnAction(e->{
            clearAll(root) ; 
            scale *= 2; 
            drawLayout(root) ; 
            
            try {
                for(int i = 0 ; i < eqs.size() ; ++i)
                    drawFunction(root , eqs.get(i) , i) ;
            } catch (Exception ex) {
                Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        
        
        TextField tf = new TextField (); 
        double tfWidth = 200 , tfHeight = 30 ; 
        tf.setMaxSize(tfWidth , tfHeight);
        tf.setMinSize(tfWidth , tfHeight);
        tf.setLayoutX(width/2 - tfWidth/2 ) ;
        tf.setLayoutY(height + equationHeight- borderOffset - tfHeight);
        
        
        double btnWidth  = 60 ; 
        Button add = new Button("ADD") ; 
        add.setLayoutX(tf.getLayoutX() + tfWidth + 10 + btnWidth + 10);
        
        add.setLayoutY(tf.getLayoutY()  );
        add.setMaxSize(btnWidth, tfHeight);
        add.setMinSize(btnWidth , tfHeight);
        
        
        add.setOnAction(e ->{
            try { 
                String tempS = prepare(tf.getText()) ; 
                eqs.add(tempS) ; 
                drawFunction(root , tempS, eqs.size() - 1) ;
                tf.setText("");
            } catch (Exception ex) {
                Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
        
        add.setDisable(true);
        
        Button newBtn = new Button("NEW") ; 
        newBtn.setLayoutX(tf.getLayoutX() + tfWidth + 10);
        newBtn.setLayoutY(tf.getLayoutY()  );
        newBtn.setMaxSize(btnWidth, tfHeight);
        newBtn.setMinSize(btnWidth , tfHeight);
        
        newBtn.setOnAction(e ->{
            try { 
                clearAll(root) ; 
                 drawLayout(root) ; 
                eqs = new ArrayList<>() ; 
                String tempS = prepare(tf.getText()) ; 
                eqs.add(tempS) ; 
                drawFunction(root , tempS, 0) ;
                add.setDisable(false);
                tf.setText("");
            } catch (Exception ex) {
                Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
        
        NodeSpeedUp(zoomIn);
        NodeSpeedUp(zoomOut);
        NodeSpeedUp(tf);
        NodeSpeedUp(add);
        NodeSpeedUp(newBtn);
        
        root.getChildren().addAll(zoomIn , zoomOut , tf , add , newBtn) ; 
    }
    
       public static String prepare(String s){
        StringBuilder ans = new StringBuilder() ; 
        boolean building = false ; 
        for(int i = 0 ; i < s.length() ; ++i){
            
            if(s.charAt(i) >= 'A' && s.charAt(i) <= 'z'){
                if(!building){
                 if(s.charAt(i) !='x' && s.charAt(i) !='X'){
                ans.append("Math.");
                building = true ; 
                 } 
                }
            }else{
                building = false ; 
            }
                ans.append(s.charAt(i)) ; 
            
        }
        
        return ans.toString() ; 
    }
    
    public void NodeSpeedUp(Node node){
        node.setCache(true);
        node.setCacheHint(CacheHint.SPEED);
    }
    
    
    public double numberWidth(String number){
        return number.length() * charWidth ; 
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
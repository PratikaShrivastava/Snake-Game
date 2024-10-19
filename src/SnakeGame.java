import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
        this.x=x;
        this.y=y;
    }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;
    boolean showFood = true;  // For blinking the food
    int blinkCounter = 0;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver= false;
    
    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth= boardWidth;
        this.boardHeight= boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,  this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        

        snakeHead= new Tile(5 ,5);
        snakeBody= new ArrayList<Tile>();

        food= new Tile(10,10);
        random= new Random();
        placeFood();

        velocityX=0;
        velocityY=0;

        gameLoop= new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        //Grid
       /*  for(int i=0; i<boardWidth/tileSize; i++){
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }*/

        //food
       /*  g.setColor(Color.red);
       // g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fillOval(food.x*tileSize, food.y*tileSize, tileSize, tileSize); */
         // Draw the food with blinking and highlight effect
         if (showFood) {
            g.setColor(Color.red); 
            g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        }

        //snake head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize,tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize,tileSize,true);

        //snake body
        for(int i=0; i< snakeBody.size();i++){
            Tile snakePart= snakeBody.get(i);
           // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over:" + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }
        else{
            g.drawString("Score:" + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }

    }
    public void placeFood(){
        food.x= random.nextInt(boardWidth/tileSize); //60\25=24
        food.y= random.nextInt(boardHeight/tileSize);
    }

    public boolean  collison(Tile tile1, Tile tile2){
      return tile1.x==tile2.x && tile1.y==tile2.y;
    }

    public void move() {
        // Check if the snake eats the food
        if (collison(snakeHead, food)) {
            // Add new tile in the next move
            snakeBody.add(new Tile(snakeHead.x, snakeHead.y));
            placeFood();
        }
    
        // Move the snake body
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            Tile prevSnakePart = snakeBody.get(i - 1);
            Tile currentSnakePart = snakeBody.get(i);
            currentSnakePart.x = prevSnakePart.x;
            currentSnakePart.y = prevSnakePart.y;
        }
    
        // Move the first part of the body to the head's current position
        if (snakeBody.size() > 0) {
            snakeBody.get(0).x = snakeHead.x;
            snakeBody.get(0).y = snakeHead.y;
        }
    
        // Move the snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    
        // Check for collision with the body
        for (int i = 0; i < snakeBody.size(); i++) {
            if (collison(snakeHead, snakeBody.get(i))) {
                gameOver = true;
            }
        }
    
        // Check for wall collisions (game over conditions)
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize || 
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

          // Blinking logic for food: toggle visibility every 3 ticks
          blinkCounter++;
          if (blinkCounter >= 3) {
              showFood = !showFood;
              blinkCounter = 0;  // Reset the counter
          }

        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!= 1){
            velocityX=0;
            velocityY=-1;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!= -1){
            velocityX=0;
            velocityY=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!= 1){
            velocityX=-1;
            velocityY=0;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!= -1){
            velocityX=1;
            velocityY=0;
        }
    }


    //do not need
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
    
}

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CT_Assignment_2TEST extends PApplet {

//node to define the root, paths and current position
BTNode root, placeInStory, iterator;

//array to contain all the scenes in the narrative
scene[] story = new scene[17];

//arrays for all items and the item boxes they will inhabit
Item[] items = new Item[4];
ItemBox[] boxes = new ItemBox[4];
//defines if player is holding an item
boolean usingItem = false;
//checks curser position
Coord curserPos;
//used for the "GoBack" button
Button prev;
//variable to define colour of curser, will change when objects are hovered
int curserCol = color(255, 255, 255);
//images that aren't in any of the scenes
PImage menu, menuHover;
//boolean used to check if the "start" has been clicked on the menu
boolean startGame;

//Intro 1 objects
Object chest;

//Intro 1-A objects
Object guard1, guard2;

//Instro 1-A-A objects
Object rock, rock2, shore;

//island objects
Object tree, tree2, tree3, water, bird, bird2, bird3, chicken;

//island-B objects
Object shrubA, shrubB, shrubs, shovel, monkeyStand;

//island-B-B objects
Object water2, monkey, gunProp;

//island-B-A objects

//cave objects
Object door, doorOpened;

//caveB objects
Object skeleton1, skeleton2;

//caveB-A objects
Object keyProp;

//caveA objects
Object mapProp;

//finale objects
Object skull, ship, guard3, water3;
public void setup() {

  

  menu = loadImage("Menu.png");
  menuHover = loadImage("MenuHover.png");
  startGame = false;

  //setup of all the scenes in the story array, these will be traversed using binary tree nodes.

  story[0] = new scene("Intro", "You have found a treasure chest that belongs to the imperial army. You hear a voice from behind, 'Stop right there'", "Turn around with your hands up", "Attempt to run", "bg1.png", "blank.png");
  story[1] = new scene("Intro-A", "You turn around to notice that you are being held at gunpoint, the army exclaims 'You're being exiled for stealing from the crown'", "Continue", " ", "bg1.png", "blank.png");
  story[2] = new scene("Intro-B", "You attempt to run away with a small amount of the gold. You are shot dead.", "Click here to return to previous scene", " ", "blank.png", "gameOver.png");
  story[3] = new scene("Intro-A-A", "You are taken on a journey to a far island. 'There is long lost treasure here that we need', the guard exclaims 'Find it before we return and we'll let you off the hook for your crimes. Else, you'll be swimming with the fishes'", "Continue", " ", "bg2.png", "fg1.png");
  story[4] = new scene("Island", "Explore the island and find the treasure. When the curser changes to red, click on the screen to interact with objects.", "Enter Cave", "Follow path", "bg3.png", "blank.png");
  story[5] = new scene("Island-B", "As you wade through the shrubs, you come across an oasis in which something is shining. From here, the path splits in two. To the right, there also seems to be a monkey fiddling with what looks to be a gun. Could be useful.", "Go deeper in to the forest", "Go to the north shoreline", "bg4.png", "blank.png");
  story[6] = new scene("IslandB-A", "More jungle, nothing to see here. This area does seem to connect to two paths, though.", "Take path to south shore", "Take path to north shore", "bg6.png", "fg4.png");
  story[7] = new scene("IslandB-B", "The northern shore. It would appear monkeys run through here. Other than the path you used to get here, there seems to only be one other direction.", "Take path to jungle depths", " ", "bg5.png", "fg3.png");
  story[8] = new scene("cave", "You enter the cave, there seems to be a locked door and a further cavern that just leads to darkness. It could be dangerous to go there with no weapon.", " ", "Enter the cavern", "bg7.png", "blank.png");
  story[9] = new scene("caveB", "You venture deep in to the cave, but are jumped by skeletons! Use a weapon to kill them or run before they reach you!", "", "", "bg8.png", "blank.png");
  story[10] = new scene("caveB-A", "The room is virtually pitch black and there appears to be no exits, but there does seem to be something shining in the distance.", " ", " ", "bg9.png", "blank.png");
  story[11] = new scene("caveA", "This is it... The map to the hidden treasure", " ", " ", "bg10.png", "blank.png");
  story[12] = new scene("Finale", "The hidden treasure. A skull, but it seems to be glowing.. You should take it to the shore", "Head to the shore", " ", "bg11.png", "blank.png");
  story[13] = new scene("Finale-A", "You arrive at the shore. The skull starts glowing rapidly.. It seems to hold some sort of great power. From the water, a cursed ship emerges. It must've been created by the magic of the skull", "Continue", " ", "bg12.png", "blank.png");
  story[14] = new scene("Finale-A-A", "The guards have also arrived. 'Hand over the skull, and we'll free you of your sentence'.", "Hand the skull over", "Jump on the cursed ship", "bg12.png", "blank.png");
  story[15] = new scene("Finale-A-A-A", "You hand the guards their skull and are brought back to mainland. You can now live a peaceful life.", "Fin.", " ", "bgFinal.png", "blank.png");
  story[16] = new scene("Finale-A-A-B", "You hold on the skull and jump on the boat. With no input, the boat starts to move on its own. You continue your life as a treasure stealing pirate, destined to forever have a bounty on your head", "Fin.", " ", "bgFinal.png", "blank.png");

  //create items

  items[0] = new Item("Pistol", "pistolItem.png");
  items[1] = new Item("Key", "keyItem.png");
  items[2] = new Item("Shovel", "shovelItem.png");
  items[3] = new Item("Map", "mapItem.png");

  //make sure the items aren't aquired form the start

  items[0].aquired = false;
  items[1].aquired = false;
  items[2].aquired = false;
  items[3].aquired = false;

  //define position of item box UI

  boxes[0] = new ItemBox(32, 598, items[0]);
  boxes[1] = new ItemBox(96, 598, items[1]);
  boxes[2] = new ItemBox(160, 598, items[2]);
  boxes[3] = new ItemBox(224, 598, items[3]);

  //Chunk of code defines the entire structure of the narrative
  //Starts by creating the root, then an iterator defines all nodes after the root
  //place in story is tracked using a curser and starts out at the root

  root = new BTNode(story[0], root);

  iterator = root;
  iterator.left = new BTNode(story[1], iterator);
  iterator.right = new BTNode(story[2], iterator);

  iterator = root.left;
  iterator.left = new BTNode(story[3], iterator);

  iterator = root.right;
  iterator.left = root;

  iterator = root.left.left;
  iterator.left = new BTNode(story[4], iterator);

  iterator = root.left.left.left;
  iterator.left = new BTNode(story[8], iterator);
  iterator.right = new BTNode(story[5], iterator);

  iterator = root.left.left.left.left;
  iterator.left = new BTNode(story[11], iterator);
  iterator.right = new BTNode(story[9], iterator);

  iterator = root.left.left.left.left.right;
  iterator.left = new BTNode(story[10], iterator);

  iterator = root.left.left.left.right;
  iterator.left = new BTNode(story[6], iterator);
  iterator.right = new BTNode(story[7], iterator);

  iterator = root.left.left.left.right.left;
  iterator.left = root.left.left.left;
  iterator.right = root.left.left.left.right.right;

  iterator = root.left.left.left.right.right;
  iterator.left = root.left.left.left.right.left;

  placeInStory = root;

  prev = new Button(1200, 600, 100, 30);

  curserPos = new Coord(mouseX, mouseY);

  //Next lines are for defining objects that will be used in different scenes

  //scene 1 objects
  chest = new Object(width/2, height/2, 512, 384, "chest.png");

  //scene 1-A objects
  guard1 = new Object(0, height, 32*8, 64*8, "Guard.png");
  guard2 = new Object(1280, height, 32*8, 64*8, "Guard.png");

  //scene 1-A-A objects
  rock = new Object(100, 400, 128, 128, "rockAtSee.png");
  rock2 = new Object(1100, 100, 128, 128, "rockAtSee.png");
  shore = new Object(width/2, -300, 1280, 540, "islandShore.png");

  //island objects
  bird = new Object(width/2, 120, 128, 128, "bird1.png");
  bird2 = new Object(width/2+30, 130, 128, 128, "bird1.png");
  bird3 = new Object(width/2-30, 110, 128, 128, "bird1.png");
  chicken = new Object(200, 500, 64, 64, "chicken1.png");
  tree = new Object(900, 400, 512, 576, "tree.png");
  tree2 = new Object(690, 220, 512/4, 576/4, "tree.png");
  tree3 = new Object(1100, 250, 512/2, 576/2, "tree.png");
  water = new Object(width/2, 400, 1280, 540, "water.png");

  //island-B objects
  shrubA = new Object(width/2, height/2, 1280, 540, "shrubL.png");
  shrubB = new Object(width/2, height/2, 1280, 540, "shrubR.png");
  shrubs = new Object(width/2, height/2, 1280, 540, "fg2.png");
  shovel = new Object(width/2, 500, 64, 64, "shovel1.png");
  monkeyStand = new Object(1150, 150, 128, 64, "monkeyStand1.png");

  //island-B-B objects
  water2 = new Object(width/2, height/2+38, 1280, 540, "water2.png");
  monkey = new Object(435, 380, 128*2, 64*2, "monkey1.png");
  gunProp = new Object(435, 500, 64, 64, "pistolItem.png");

  //cave objects
  door = new Object(width/2, height/2, 1280, 540, "door1.png");
  doorOpened = new Object(width/2, height/2, 1280, 540, "door2.png");

  //caveB objects
  skeleton1 = new Object(width/4-100, height/2, 256, 288, "skeleton.png");
  skeleton2 = new Object(width/4*3+100, height/2, 256, 288, "skeleton.png");

  //caveBA objects
  keyProp = new Object(width/2, 400, 64, 64, "keyItem.png");

  //caveA objects
  mapProp = new Object(width/2, height/2, 64, 64, "mapItem.png");

  //finale objects
  skull = new Object(width/2, 720, 1280, 540, "cursedSkull.png");
  ship = new Object(1350, height/2, 1280, 540, "cursedShip.png");
  guard3 = new Object(0, height/2, 32*8, 64*8, "Guard.png");
  water3 = new Object(width/2, height/2+80, 1280, 540, "water2.png");
}

public void draw() {
  if (!startGame) { //displays the menu if the game hasn't started
    displayMenu();
  } else if (startGame) {
    noCursor();
    curserPos.x = mouseX; //updates curserposition variable
    curserPos.y = mouseY;
    background(255);

    //next chunk of code creates the scenes that are currently being displayed and creates the buttons inside of them
    placeInStory.value.drawBackground();
    for (int i = 0; i < placeInStory.value.buttonsInScene.length; i++) {
      placeInStory.value.buttonsInScene[i].checkHover();
    }
    placeInStory.value.select1.checkHover();
    placeInStory.value.select2.checkHover();
    for (int i = 0; i < 4; i++) {
      boxes[i].drawBox();
    }

    //next lines update the curser sprite depending on if an item is equipped or not

    for (int i = 0; i < 4; i++) {
      items[i].checkEquipped();
    }

    if (!items[0].equipped && !items[1].equipped && !items[2].equipped && !items[3].equipped) {
      stroke(0);
      strokeWeight(1);
      for (int i = 0; i < placeInStory.value.buttonsInScene.length; i++) {
        if (mouseX > placeInStory.value.buttonsInScene[i].pos.x-(placeInStory.value.buttonsInScene[i].size.x/2) && mouseX < placeInStory.value.buttonsInScene[i].pos.x+(placeInStory.value.buttonsInScene[i].size.x/2) && mouseY > placeInStory.value.buttonsInScene[i].pos.y-(placeInStory.value.buttonsInScene[i].size.y/2) && mouseY < placeInStory.value.buttonsInScene[i].pos.y+(placeInStory.value.buttonsInScene[i].size.y/2)) {
          curserCol = color(255, 0, 0);
        }
      }

      //if item is not equipped draws a triangle for a curser
      fill(curserCol);
      triangle(mouseX, mouseY, mouseX-20, mouseY+10, mouseX-10, mouseY+20);
      curserCol = color(255, 255, 255);
    }
  }
}

//key options can be uncommented for keyboard use instead of mouse

//void keyPressed() {
//  if (key == '1') { 
//    if (placeInStory.left != null) { 
//      placeInStory = placeInStory.left; 
//      placeInStory.value.setup = true;
//    }
//  } else if (key == '2') { 
//    if (placeInStory.right != null) { 
//      placeInStory = placeInStory.right; 
//      placeInStory.value.setup = true;
//    }
//  } else if (key == 'b' || key == 'B') {
//    if (placeInStory.prev!=null) { 
//      placeInStory = placeInStory.prev; 
//      placeInStory.value.setup = true;
//    }
//  }
//}

//mouse released event function is called and checks whether any buttons have been clicked
//if the first option on a scene is clicked it will progress the story left
//if the second option is clicked it will progress the story right
//if an item box is equipped it will set that item to being equipped
public void mouseReleased() {
  for (int i = 0; i < placeInStory.value.buttonsInScene.length; i++) {
    if (mouseX > placeInStory.value.buttonsInScene[i].pos.x-(placeInStory.value.buttonsInScene[i].size.x/2) && mouseX < placeInStory.value.buttonsInScene[i].pos.x+(placeInStory.value.buttonsInScene[i].size.x/2) && mouseY > placeInStory.value.buttonsInScene[i].pos.y-(placeInStory.value.buttonsInScene[i].size.y/2) && mouseY < placeInStory.value.buttonsInScene[i].pos.y+(placeInStory.value.buttonsInScene[i].size.y/2)) {
      placeInStory.value.buttonsInScene[i].clicked = true;
    }
  }
  if (mouseX > placeInStory.value.select1.pos.x-(placeInStory.value.select1.size.x/2) && mouseX < placeInStory.value.select1.pos.x+(placeInStory.value.select1.size.x/2) && mouseY > placeInStory.value.select1.pos.y-(placeInStory.value.select1.size.y/2) && mouseY < placeInStory.value.select1.pos.y+(placeInStory.value.select1.size.y/2)) {
    if (placeInStory.left != null) {
      if (placeInStory.value.panelName == "caveB") { //exception to the rule as this option will only appear once skeletons have been defeated
        if (placeInStory.value.sceneChange == true) {
          placeInStory = placeInStory.left; 
          placeInStory.value.setup = true;
        }
      } else if (placeInStory.value.panelName == "cave") { //exception to the rule as this option will only appear once door is opened
        if (placeInStory.value.sceneChange == true) {
          placeInStory = placeInStory.left; 
          placeInStory.value.setup = true;
        }
      } else { 
        placeInStory = placeInStory.left; 
        placeInStory.value.setup = true;
      }
    }
  }
  if (mouseX > placeInStory.value.select2.pos.x-(placeInStory.value.select2.size.x/2) && mouseX < placeInStory.value.select2.pos.x+(placeInStory.value.select2.size.x/2) && mouseY > placeInStory.value.select2.pos.y-(placeInStory.value.select2.size.y/2) && mouseY < placeInStory.value.select2.pos.y+(placeInStory.value.select2.size.y/2)) {
    if (placeInStory.right != null) {
      if (placeInStory.value.panelName == "caveB") {
        if (placeInStory.value.sceneChange == true) {
          placeInStory = placeInStory.right; 
          placeInStory.value.setup = true;
        }
      } else { 
        placeInStory = placeInStory.right; 
        placeInStory.value.setup = true;
      }
    }
  }
  for (int i = 0; i < 4; i++) {
    if (mouseX > boxes[i].pos.x-(boxes[i].size.x/2) && mouseX < boxes[i].pos.x+(boxes[i].size.x/2) && mouseY > boxes[i].pos.y-(boxes[i].size.y/2) && mouseY < boxes[i].pos.y+(boxes[i].size.y/2)) {
      if (usingItem) {
        boxes[0].item.equipped = false; 
        boxes[1].item.equipped = false; 
        boxes[2].item.equipped = false; 
        boxes[3].item.equipped = false; 
        usingItem = false;
      } else if (!usingItem && boxes[i].item.aquired) {
        boxes[i].item.equipped = true;
        usingItem = true;
      }
    }
  }
  if (mouseX > prev.pos.x-(prev.size.x/2) && mouseX < prev.pos.x+(prev.size.x/2) && mouseY > prev.pos.y-(prev.size.y/2) && mouseY < prev.pos.y+(prev.size.y/2)) {
    if (placeInStory.prev!=null) { 
      placeInStory = placeInStory.prev; 
      placeInStory.value.setup = true;
    }
  }
}

//code for the menu
public void displayMenu() {
  Button start = new Button(400, 300, 600, 250);
  start.checkHover();
  imageMode(CENTER);
  image(menu, width/2, height/2);
  if (start.hover) {
    image(menuHover, width/2, height/2); 
    if (mousePressed) {
      startGame = true;
    }
  }
}
//Coord data structure is used for storing two different variables, not necessarily just positions but also for size and vectors
class Coord {
  int x;
  int y;
  Coord(int p1, int p2) {
    x = p1;
    y = p2;
  }
}

//Tree node
class BTNode {
  BTNode left;
  BTNode right;
  BTNode prev;
  scene value;

  BTNode(scene p1, BTNode p2) {
    prev = p2; //stores the previous scene
    value = p1; //stores a scene inside the node
    left = null;
    right = null;
  }

  public void setLeft(BTNode p1) { //methods allow nodes to change their addresses
    left = p1;
  }

  public void setRight(BTNode p1) {
    right = p1;
  }

  public void setPrev(BTNode p1) {
    prev = p1;
  }
}

//stopwatch class can be used if a timer is needed
class Stopwatch {

  int time; 
  int waitTime;
  int lastTime;
  boolean active;

  Stopwatch(int p1) { //time is input here
    active = true;
    waitTime = p1;
    time = waitTime;
  }

  public void startClock() {
    if (time <= waitTime && time > 0) { //time is updated every frames so 30 time = 1 second
      time--;
    } else if (time == 0) {
      active = false;
    }
  }

  public void resetClock() {
    time = waitTime;
  }

  public boolean getSignal() {
    return active;
  }
}
//Button class is used whenever program needs to craw a button.
class Button {
  Coord pos;
  Coord size;
  boolean clicked;
  boolean hover;

  Button(int p1, int p2, int p3, int p4) {
    clicked = false;
    hover = false;
    pos = new Coord(p1, p2);
    size = new Coord(p3, p4);
  }

  public void checkHover() {
    if (mouseX > pos.x-(size.x/2) && mouseX < pos.x+(size.x/2) && mouseY > pos.y-(size.y/2) && mouseY < pos.y+(size.y/2)) { //Will send out a signal whenever the button is hovered over
      hover = true;
    } else {
      hover = false;
    }
  }

  public void drawHitboxes() { //drawing hitboxes is used for testing only
    noFill();
    stroke(255, 0, 0);
    strokeWeight(5);
    rectMode(CENTER);
    rect(pos.x, pos.y, size.x, size.y);
  }
}

class Item { //items are defined with a sprite but is mainly used to acess if the item is aquired or if it is equipped
  String name;
  PImage sprite;
  boolean equipped;
  boolean aquired;
  Item(String p1, String p2) {
    equipped = false;
    aquired = false;
    name = p1;
    sprite = loadImage(p2);
  }

  public void checkEquipped() { //changes the mouse to item sprite if equipped
    if (equipped) {
      image(sprite, mouseX, mouseY, 64, 64);
    }
  }
}

class ItemBox { //used for the UI
  Coord pos; 
  Item item;
  boolean chosen;
  Coord size;
  Button button;
  PImage noItem;

  ItemBox(int p1, int p2, Item p3) {
    chosen = false;
    item = p3;
    pos = new Coord(p1, p2);
    size = new Coord(64, 64);
    button = new Button(pos.x, pos.y, size.x, size.y);
    noItem = loadImage("noItem.png"); //incase no item has been obtained
  }

  public void drawBox() { //draws the UI
    button.checkHover();
    fill(150, 50);
    if (item.equipped == true) {
      fill(200, 70);
    }
    rectMode(CENTER);
    strokeWeight(1);
    stroke(0);
    rect(pos.x, pos.y, 64, 64); 
    if (item.aquired) {
      imageMode(CENTER);
      image(item.sprite, pos.x, pos.y, size.x, size.y);
    } else if (!item.aquired) {
      imageMode(CENTER);
      image(noItem, pos.x, pos.y, size.x, size.y);
    }
  }
}

class Object { //Main class used for drawing things
  int iterate = 0;
  PImage sprite;
  Coord pos;
  Coord size;
  Coord moveTo;
  Coord scaleTo;
  boolean moveBack, scaleBack;
  Coord startPos;
  Coord startSize;

  Object(int p1, int p2, int p3, int p4, String p5) { //base object construction for anything that doesn't need to move or scale.
    pos = new Coord(p1, p2);
    size = new Coord(p3, p4);
    sprite = loadImage(p5);
    moveTo = new Coord(pos.x, pos.y);
    startPos = new Coord(p1, p2);
    moveBack = false;
    scaleBack = false;
    startSize = new Coord(p3, p4);
    scaleTo = new Coord(size.x, size.y);
  }

  public void drawObject() {
    image(sprite, pos.x, pos.y, size.x, size.y);
  }

  public void resetObject() { //this is called if you need the object to go back to its original position
    pos.x = startPos.x;
    pos.y = startPos.y;
    size.x = startSize.x;
    size.y = startSize.y;
  }

  public void moveAlongVector(int p1, int p2, boolean p3) {
    boolean doesMoveBack = p3;
    moveTo.x = p1;
    moveTo.y = p2;

    if (!moveBack) { //checks if the object is in the position you want it to go, if it is not increases/decreases values towards that position
      if (pos.x > moveTo.x) {
        pos.x--;
      }
      if (pos.x < moveTo.x) {
        pos.x++;
      }
      if (pos.y > moveTo.y) {
        pos.y--;
      }
      if (pos.y < moveTo.y) {
        pos.y++;
      }
      if (pos.x == moveTo.x && pos.y == moveTo.y) {
        moveBack = true;
      }
    } else if (moveBack && doesMoveBack) { //if object is told to move back to original position, does the same as before but opposite
      if (moveBack) {
        if (pos.x > startPos.x) {
          pos.x--;
        }
        if (pos.x < startPos.x) {
          pos.x++;
        }
        if (pos.y > startPos.y) {
          pos.y--;
        }
        if (pos.y < startPos.y) {
          pos.y++;
        }
        if (pos.x == startPos.x && pos.y == startPos.y) {
          moveBack = false;
        }
      }
    }
  }

  public void scaleAlongVector(int p1, int p2, boolean p3) { //the same concept as moving, only for the size
    boolean doesMoveBack = p3;
    scaleTo.x = p1;
    scaleTo.y = p2;

    if (!scaleBack) {
      if (size.x > scaleTo.x) {
        size.x--;
      }
      if (size.x < scaleTo.x) {
        size.x++;
      }
      if (size.y > scaleTo.y) {
        size.y--;
      }
      if (size.y < scaleTo.y) {
        size.y++;
      }
      if (size.x == scaleTo.x && size.y == scaleTo.y) {
        scaleBack = true;
      }
    } else if (scaleBack && doesMoveBack) {
      if (scaleBack) {
        if (size.x > startSize.x) {
          size.x--;
        }
        if (size.x < startSize.x) {
          size.x++;
        }
        if (size.y > startSize.y) {
          size.y--;
        }
        if (size.y < startSize.y) {
          size.y++;
        }
        if (size.x == startSize.x && size.y == startSize.y) {
          scaleBack = false;
        }
      }
    }
  }

  public void animate(String[] p1, boolean p2) { //function is called if the object should be animated
    boolean repeat = p2;
    PImage[] images = new PImage[p1.length]; //array is set to hold the amount of images defined
    for (int i = 0; i < images.length; i++) {
      images[i] = loadImage(p1[i]);
    }
    sprite = images[iterate/4]; //updates sprite every 4 frames
    if (iterate < images.length*4) {
      iterate++;
    }
    if (iterate == images.length*4 && repeat) { // repeats the process from beginning if it is told to
      iterate = 0;
    } else if (iterate == images.length*4 && !repeat) { //stops animating if told not to repeat
      iterate = images.length*4-1;
    }
  }
}
class scene {
  //attributes
  String panelName;
  String infoText, originalInfoText;
  String optionText1, optionText2;
  Button select1, select2;
  PImage background;
  PImage foreground;
  Button[] buttonsInScene;
  boolean setup;
  boolean sceneChange;
  boolean gameOver;
  Stopwatch watch;

  //constructor
  scene(String p1, String p2, String p3, String p4, String p5, String p6) {
    select1 = new Button(width/2, 655, 1280, 30);
    select2 = new Button(width/2, 685, 1280, 30);
    panelName = p1;
    infoText = p2;
    originalInfoText = p2;
    optionText1 = p3;
    optionText2 = p4;
    background = loadImage(p5);
    foreground = loadImage(p6);
    if (panelName == "Island") { //this changes the size of the buttonsInScene array and the buttons within it depending on which scene it is. This allows for different buttons in each scene
      println("creating island buttons");
      buttonsInScene = new Button[3];
      buttonsInScene[0] = new Button(250, 300, 650, 200);
      buttonsInScene[1] = new Button(900, 400, 512, 476);
      buttonsInScene[2] = new Button(200, 500, 64, 64);
    } else if (panelName == "Island-B") {
      println("creating islandB buttons");
      buttonsInScene = new Button[2];
      buttonsInScene[0] = new Button(width/2, 500, 128, 128);
      buttonsInScene[1] = new Button(1150, 150, 128, 64);
    } else if (panelName == "IslandB-B") {
      println("craetingislandB-B buttons");
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(435, 500, 64, 64);
    } else if (panelName == "cave") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(400, 300, 200, 350);
    } else if (panelName == "caveB") {
      buttonsInScene = new Button[2];
      buttonsInScene[0] = new Button(width/4, height/2, width/2, 350);
      buttonsInScene[1] = new Button(width/4*3, height/2, width/2, 350);
    } else if (panelName == "caveB-A") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(width/2, 400, 64, 64);
    } else if (panelName == "caveA") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(width/2, height/2, 64, 64);
    } else {
      buttonsInScene = new Button[0];
    }
  }

  //methods

  public void drawBackground() {
    background(255);
    image(background, width/2, height/2); //the base of every scene, draws objects and backgrounds
    drawObjects();
    fill(0);

    rectMode(CORNERS);
    rect(0, 0, width, height/8);
    rect(0, (height/8)*7, width, height); //draws black bars

    //next lines are all used for the text that is going to be displayed at the top and bottom
    fill(255);
    textAlign(CENTER);
    textSize(40);
    textSize(20);
    text(panelName, width/2, 25);
    textSize(22);
    text(infoText, 0, 30, 1280, 200);
    if (select1.hover == false) {
      textSize(22);
      text(optionText1, width/2, 660);
    } else if (select1.hover == true) { //makes the text change size when it is hovered over
      textSize(25);  
      text(optionText1, width/2, 660);
    }
    if (select2.hover == false) {
      textSize(22);
      text(optionText2, width/2, 690);
    } else if (select2.hover == true) {
      textSize(25);
      text(optionText2, width/2, 690);
    }
    image(foreground, width/2, height/2);

    prev.checkHover();
    fill(0, 50);  
    rectMode(CENTER);
    rect(prev.pos.x, prev.pos.y-10, 100, 30);  
    if (!prev.hover) {
      textAlign(CENTER);
      fill(255);
      textSize(22);
      text("Go Back", prev.pos.x, prev.pos.y);
    } else if (prev.hover) {
      fill(255);
      textAlign(CENTER);
      textSize(25);
      text("Go back", prev.pos.x, prev.pos.y);
    }
    checkGameOver(); //checks if the player has died

    if (mousePressed) { //special case for if the player has the map. Clicking whilst the map is equipped will always update the info text
      if (items[3].equipped) {
        infoText = "The map seems to lead to the deepest part of the forest";
      }
    }
  }

  public void checkGameOver() { //draws an image over the top of everything when the player dies and allows them to return to the previous scene
    if (gameOver) {
      fill(0);
      rectMode(CORNERS);
      rect(0, 0, width, height/8);
      rect(0, (height/8)*7, width, height); 
      PImage endScreen = loadImage("gameOver.png");
      image(endScreen, width/2, height/2);
      infoText = "You died. Click anywhere to return to previous scene.";
      fill(255);
      textSize(22);
      text(infoText, 0, 30, 1280, 200);
      if (mousePressed) {
        placeInStory = placeInStory.prev; 
        placeInStory.value.setup = true;
        gameOver = false;
      }
    }
  }


  //draw objects is called in a different way for each scene
  //this is where all anuimation is listed
  //infotext can also be updated here
  //each scene has a setup attribute that will be called once every time the player enters that scene
  //there are some special cases in this that allow the program to function slightly differently depending on which scene the user is on


  public void drawObjects() {
    if (panelName == "Intro") {
      if (setup) {
        infoText = originalInfoText;
        chest.resetObject();
        chest.moveBack = false;
        chest.scaleBack = false;
        setup = false;
      }
      chest.drawObject();
      imageMode(CENTER);
      chest.moveAlongVector(width/2, height/2, false);
      chest.scaleAlongVector(512+64, 384+64, false);
    }

    if (panelName == "Intro-A-A") {
      if (setup) {
        infoText = originalInfoText;
        shore.resetObject();
        shore.moveBack = false;
        rock.resetObject();
        rock.moveBack = false;
        rock2.resetObject();
        rock2.moveBack = false;
        setup = false;
      }
      shore.drawObject();
      rock.drawObject();
      rock2.drawObject();
      imageMode(CENTER);
      shore.moveAlongVector(width/2, 360, false);
      rock.moveAlongVector(100, height, false);
      rock2.moveAlongVector(1100, height, false);
    }

    if (panelName == "Intro-A") {
      if (setup) {
        infoText = originalInfoText;
        guard1.resetObject();
        guard1.moveBack = false;
        guard2.resetObject();
        guard2.moveBack = false;
        setup = false;
      }
      chest.drawObject();
      guard1.drawObject();
      guard2.drawObject();
      imageMode(CENTER);
      guard1.moveAlongVector(300, 400, false);
      guard2.moveAlongVector(980, 400, false);
      guard1.moveAlongVector(300, 400, false);
      guard2.moveAlongVector(980, 400, false);
    }

    if (panelName == "Island") {
      if (setup) {
        infoText = originalInfoText; 
        buttonsInScene[0].clicked = false;
        buttonsInScene[1].clicked = false;
        bird.resetObject();
        bird.scaleBack = false;
        bird2.resetObject();
        bird2.scaleBack = false;
        bird3.resetObject();
        bird3.scaleBack = false;
        setup = false;
      }

      tree2.drawObject();
      tree3.drawObject();
      water.drawObject();
      tree.drawObject();
      String[] birdAnimate = {"bird1.png", "bird2.png", "bird3.png", "bird4.png", "bird5.png"}; // the first example of setting up an animation
      bird.animate(birdAnimate, true);
      bird.drawObject();
      bird.scaleAlongVector(0, 0, false);
      bird2.animate(birdAnimate, true);
      bird2.drawObject();
      bird2.scaleAlongVector(0, 0, false);
      bird3.animate(birdAnimate, true);
      bird3.drawObject();
      bird3.scaleAlongVector(0, 0, false);
      String[] chickenAnimate = {"chicken1.png", "chicken2.png", "chicken3.png", "chicken4.png", "chicken3.png", "chicken2.png", "chicken1.png", "chicken1.png", "chicken1.png", "chicken1.png"};
      chicken.animate(chickenAnimate, true);
      chicken.drawObject();

      water.moveAlongVector(width/2, 500, true);
      if (buttonsInScene[0].clicked) {
        infoText = "It's a cave. It could be dangerous, but exploring it will likely become necessary";
        buttonsInScene[0].clicked = false;
      }
      if (buttonsInScene[1].clicked) {
        infoText = "An ordinary palm tree, this place is littered with them"; 
        buttonsInScene[1].clicked = false;
      }
      if (buttonsInScene[2].clicked) {
        infoText = "What would you know, a chicken"; 
        if (items[0].equipped == true) {
          infoText = "You won't gain anything by killing this innocent chicken, you monster";
        }
        buttonsInScene[2].clicked = false;
      }
    }

    if (panelName == "Island-B") {
      if (setup) {
        sceneChange = false;
        infoText = originalInfoText; 
        shrubA.moveBack = false;
        shrubB.moveBack = false;
        shrubA.resetObject();
        shrubB.resetObject();
        setup = false;
      }
      if (!items[2].aquired) {
        String[] shovelAnimate = {"shovel1.png", "shovel2.png", "shovel3.png"};
        shovel.animate(shovelAnimate, true);
        shovel.drawObject();
      }
      String[] monkeyStandAnimate = {"monkeyStand1.png", "monkeyStand2.png", "monkeyStand3.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png"}; 
      monkeyStand.drawObject();
      shrubs.drawObject();
      shrubA.drawObject();
      shrubA.moveAlongVector(100, height/2, false);
      shrubA.moveAlongVector(100, height/2, false);
      shrubB.drawObject();
      shrubB.moveAlongVector(1180, height/2, false);
      shrubB.moveAlongVector(1180, height/2, false);

      if (buttonsInScene[0].clicked) {
        infoText = "It's an oasis. You see something shining in the water and grab it. You have obtained a shovel, it could be useful for digging treasure";
        items[2].aquired = true;
        buttonsInScene[0].clicked = false;
      }
      if (buttonsInScene[1].clicked) {
        infoText = "The monkey ran away! It looks like it ran to the North Shore";
        sceneChange = true;
        buttonsInScene[1].clicked = false;
      }
      if (sceneChange) {
        monkeyStand.animate(monkeyStandAnimate, true);
        monkeyStand.moveAlongVector(1500, 0, false);
      }
    }

    if (panelName == "IslandB-B") {
      if (setup) {
        setup = false;
        buttonsInScene[0].clicked = false;
      }
      water2.drawObject();
      water2.moveAlongVector(width/2, height/2-20, true);
      String[] monkeyAnimate = {"monkey1.png", "monkey2.png", "monkey3.png", "monkey4.png", "monkey3.png", "monkey2.png", "monkey1.png", "monkey5.png", "monkey6.png", "monkey7.png", "monkey6.png", "monkey5.png", "monkey1.png", };
      monkey.animate(monkeyAnimate, true);
      if (story[5].sceneChange) {
        monkey.drawObject();
        if (!items[0].aquired) {
          infoText = originalInfoText + " It would appear the monkey that ran away dropped that gun here";
          gunProp.drawObject();
        }
        if (buttonsInScene[0].clicked) {
          items[0].aquired = true;
          infoText = "You aquired a flintlock pistol. This will come in useful for dealing with danger";
          buttonsInScene[0].clicked = false;
        }
      }
    }
    if (panelName == "IslandB-A") {
      if (items[3].aquired && sceneChange == false) {
        infoText = "This seems to be where the map leads to. Perhaps you should use your shovel and try digging"; 
        sceneChange = true;
      }
      if (items[3].aquired && items[2].equipped) {
        //This code sets up the irreversable part of the story (The Fanale) and changes the tree routes or order to only allow the player to progress once they have finished the main quest  
        if (mousePressed) {
          placeInStory.left = new BTNode(story[12], null);
          placeInStory.right = null;
          placeInStory.left.left = new BTNode(story[13], null);
          placeInStory.left.left.left = new BTNode(story[14], null);
          //These two operate as the final decisions in the story
          placeInStory.left.left.left.left = new BTNode(story[15], null);
          placeInStory.left.left.left.right = new BTNode(story[16], null);
          //Story is moved forward here
          placeInStory = placeInStory.left;
        }
      }
    }

    if (panelName == "cave") {
      if (setup) {
        setup = false;
      }
      if (buttonsInScene[0].clicked && !items[1].equipped) {
        infoText = "You'll need to use a key to open this door";
        buttonsInScene[0].clicked = false;
      } else if (buttonsInScene[0].clicked && items[1].equipped) {
        sceneChange = true;
      } else if (!sceneChange) {
        door.drawObject();
      }
      if (sceneChange) {
        infoText = "You used the key to open the door, a new path is available"; 
        optionText1 = "Enter door";
        doorOpened.drawObject();
      }
    }
    if (panelName == "caveB") {
      if (setup) {
        watch = new Stopwatch(600); 
        watch.resetClock();
        skeleton1.moveBack = false;
        skeleton2.moveBack = false;
        skeleton1.scaleBack = false;
        skeleton2.scaleBack = false;
        skeleton1.resetObject();
        skeleton2.resetObject();
        setup = false;
      }
      watch.startClock();
      if (watch.active && !sceneChange) {  
        String time = str(watch.time/60);
        println(time);
        textSize(22);
        fill(255, 0, 0);
        text("Time remaining: " + time, width/2, 200);
        skeleton1.moveAlongVector(width/2-100, 600, false);
        skeleton2.moveAlongVector(width/2+100, 600, false);
        skeleton1.scaleAlongVector(256*2, 288*2, false);
        skeleton2.scaleAlongVector(256*2, 288*2, false);
        if (buttonsInScene[0].clicked && items[0].equipped) {
          if (buttonsInScene[1].clicked) {
            sceneChange = true;
          }
        } else if (!buttonsInScene[0].clicked || !items[0].equipped) {
          skeleton1.drawObject();
          buttonsInScene[0].clicked = false;
        }
        if (buttonsInScene[1].clicked && items[0].equipped) { 
          if (buttonsInScene[0].clicked) {
            sceneChange = true;
          }
        } else if (!buttonsInScene[1].clicked || !items[0].equipped) {
          skeleton2.drawObject();
          buttonsInScene[1].clicked = false;
        }
      } else if (!watch.active && !sceneChange) {
        println("lose");
        gameOver = true;
      } else if (sceneChange) {
        infoText = "You defeated the skeletons! You can now venture futher in to the cavern.";
        optionText1 = "Go deeper in to the caverns";
      }
    }

    if (panelName == "caveB-A") {
      if (setup) {
        setup = false;
      }
      if (buttonsInScene[0].clicked) {
        items[1].aquired = true;
        infoText = "You found a key in the dark room. What could it open?";
      } else {
        keyProp.drawObject();
      }
    }

    if (panelName == "caveA") {
      if (setup) {  
        setup = false;
      }
      if (buttonsInScene[0].clicked) {
        items[3].aquired = true;
        infoText = "You found the map. It seems to lead to the deepest point of the forest. If you have a shovel, go there and use it on the ground";
      } else {
        mapProp.drawObject();
      }
    }

    if (panelName == "Finale") {
      if (setup) {
        skull.moveBack = false;
        skull.resetObject();
        setup = false;
      }
      skull.drawObject();
      skull.moveAlongVector(width/2, height/2, false);
    }

    if (panelName == "Finale-A") {
      if (setup) {
        ship.moveBack = false;
        ship.resetObject();
        setup = false;
      }
      ship.drawObject();
      ship.moveAlongVector(width/2, height/2, false);
    }
    if (panelName == "Finale-A-A") {
      if (setup) {
        guard3.moveBack = false;
        guard3.resetObject();
        setup = false;
      }
      ship.drawObject();
      guard3.drawObject();
      guard3.moveAlongVector(300, height/2, false);
    }
  }
}
  public void settings() {  size(1280, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CT_Assignment_2TEST" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

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

  void checkHover() {
    if (mouseX > pos.x-(size.x/2) && mouseX < pos.x+(size.x/2) && mouseY > pos.y-(size.y/2) && mouseY < pos.y+(size.y/2)) { //Will send out a signal whenever the button is hovered over
      hover = true;
    } else {
      hover = false;
    }
  }

  void drawHitboxes() { //drawing hitboxes is used for testing only
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

  void checkEquipped() { //changes the mouse to item sprite if equipped
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

  void drawBox() { //draws the UI
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

  void drawObject() {
    image(sprite, pos.x, pos.y, size.x, size.y);
  }

  void resetObject() { //this is called if you need the object to go back to its original position
    pos.x = startPos.x;
    pos.y = startPos.y;
    size.x = startSize.x;
    size.y = startSize.y;
  }

  void moveAlongVector(int p1, int p2, boolean p3) {
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

  void scaleAlongVector(int p1, int p2, boolean p3) { //the same concept as moving, only for the size
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

  void animate(String[] p1, boolean p2) { //function is called if the object should be animated
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
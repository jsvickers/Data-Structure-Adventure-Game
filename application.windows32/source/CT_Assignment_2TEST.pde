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
color curserCol = color(255, 255, 255);
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
void setup() {

  size(1280, 720);

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

void draw() {
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
void mouseReleased() {
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
void displayMenu() {
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

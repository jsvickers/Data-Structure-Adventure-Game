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

  void setLeft(BTNode p1) { //methods allow nodes to change their addresses
    left = p1;
  }

  void setRight(BTNode p1) {
    right = p1;
  }

  void setPrev(BTNode p1) {
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

  void startClock() {
    if (time <= waitTime && time > 0) { //time is updated every frames so 30 time = 1 second
      time--;
    } else if (time == 0) {
      active = false;
    }
  }

  void resetClock() {
    time = waitTime;
  }

  boolean getSignal() {
    return active;
  }
}

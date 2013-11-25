package eecs285.proj4.jminjie;

public class Siege {
  public static void main(String args[]){
    
    final int rows = 30;
    final int cols = 50;
    Tile[][] mapTiles = new Tile[rows][cols];
    for (int i = 0; i < rows; i++){
      for (int j = 0; j < cols; j++){
        mapTiles[i][j] = new TileMuddy();
      }
    }
    MainGameFrame mainFrame = new MainGameFrame("Siege Test", rows, cols, mapTiles);
    return;
  }
}

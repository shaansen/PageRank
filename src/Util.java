/* Function :Java class to print output in matrix form.
 * Input    : 2D array of floats for normalized values of pages based on scores of the weighted factors
 * Display  : Matrix representation of the array
 * Sources  : https://github.com/yfeng55/Weighted-Pagerank
 * */
public class Util {

    public static void print2DArray(float [][] array){

        if(array == null || array.length < 1 || array[0].length < 1){
            throw new IllegalArgumentException("array is empty");
        }

        for (int i=0; i< array.length; i++){

            for(int j=0; j<array[0].length; j++){
                System.out.print("[" + array[i][j] + "]");
            }

            System.out.println();
        }

    }


}


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

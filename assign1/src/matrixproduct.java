public class MatrixProduct{
    
    public static void OnMult(int m_ar, int m_br) {
        
        long Time1, Time2;
        
        double temp;
        double[] pha, phb, phc;
        
        pha = new double[m_ar * m_ar];
        phb = new double[m_ar * m_ar];
        phc = new double[m_ar * m_ar];
        
        for(int i = 0; i < m_ar; i++){
            for(int j = 0; j < m_ar; j++){
                pha[i * m_ar + j] = 1.0;
            }    
        }

        for(int  i = 0; i < m_br; i++){
            for( int  j= 0; j < m_br; j++){
                phb[i * m_br + j] = i + 1;
            }    
        }

        Time1 = System.currentTimeMillis();
        
        for(int  i = 0; i < m_ar; i++) {
            for(int  j = 0; j < m_br; j++) {
                temp = 0;
                for(int  k = 0; k < m_ar; k++) {
                    temp += pha[i * m_ar + k] * phb[k * m_br + j];
                }
                phc[i * m_ar + j] = temp;
            }
        }
        
        Time2 = System.currentTimeMillis();
        System.out.printf("Time: %.3f seconds%n", (double) (Time2 - Time1) / 1000.0);

        System.out.println("Result matrix: ");
        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < Math.min(10, m_br); j++){
                System.out.printf(phc[j] + " ");
            }    
        }
        System.out.println();
    }

    public static void OnMultLine(int m_ar, int m_br){
        
        long Time1, Time2;

        double[] pha,phb,phc;
        
        pha = new double[m_ar * m_ar];
        phb= new double[m_ar * m_ar];
        phc = new double[m_ar * m_ar];

        for(int i=0; i<m_ar; i++)
            for(int j=0; j<m_ar; j++)
                pha[i*m_ar + j] = 1.0;

        for(int i=0; i<m_br; i++)
            for(int j=0; j<m_br; j++)
                phb[i*m_br + j] = (i+1);
        
        for(int i=0; i<m_br; i++)
            for(int j=0; j<m_br; j++)
                phc[i*m_br + j] = 0.0;
        
        Time1 = System.currentTimeMillis();

        for (int i = 0; i < m_ar; i++) {
            for (int j = 0; j < m_br; j++) {
                phc[i * m_ar + j] = 0;
                for (int k = 0; k < m_ar; k++) {
                    phc[i * m_ar + j] += a[i * m_ar + k] * phb[k * m_br + j];
                }
            }
	    }

        Time2 = System.currentTimeMillis();
        
        System.out.printf("Time: %.3f seconds%n", (double) (Time2 - Time1) / 1000.0);

        System.out.println("Result matrix: ");
        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < Math.min(10, m_br); j++){
                System.out.printf(phc[j] + " ");
            }    
        }
        System.out.println();

    }

    public static void OnMultBlock(int m_ar, int m_br, int bkSize){
        
        long Time1, Time2;

        double[] a,b,c;
        
        a = new double[m_ar * m_ar];
        b = new double[m_ar * m_ar];
        c = new double[m_ar * m_ar];

        for(int i=0; i<m_ar; i++)
            for(int j=0; j<m_ar; j++)
                a[i*m_ar + j] = 1.0;

        for(int i=0; i<m_br; i++)
            for(int j=0; j<m_br; j++)
                b[i*m_br + j] = (i+1);
        
        for(int i=0; i<m_br; i++)
            for(int j=0; j<m_br; j++)
                c[i*m_br + j] = 0.0;
        
        Time1 = System.currentTimeMillis();

        for (int i = 0; i < m_ar; i += bkSize) {
            for (int j = 0; j < m_br; j += bkSize) {
                for (int k = 0; k < m_ar; k += bkSize) {
                    for (int ii = i; ii < Math.min(i + bkSize, m_ar); ii++) {
                        for (int jj = j; jj < Math.min(j + bkSize, m_br); jj++) {
                            for (int kk = k; kk < Math.min(k + bkSize, m_ar); kk++) {
                                c[ii * m_ar + jj] += a[ii * m_ar + kk] * b[kk * m_br + jj];
                            }
                        }
                    }
                }
            }
        }

        Time2 = System.currentTimeMillis();
        
        System.out.printf("Time: %.3f seconds%n", (double) (Time2 - Time1) / 1000.0);

        System.out.println("Result matrix: ");
        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < Math.min(10, m_br); j++){
                System.out.printf(c[j] + " ");
            }    
        }
        System.out.println();
    }

    public static void main(String[] args){
        char c;
        int lin, col, blockSize;
        int op;

        op=1;
        do{
            System.out.println();
            System.out.println("1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.println("3. Block Multiplication");
            System.out.println("Selection?: ");
            op=readInt();
            if(op==0){
                break;
            }
            System.out.print("Dimensions: lins=cols ? ");
            lin=readInt();
            col=lin;

            switch(op){
            case 1:
                OnMult(lin,col);
                break;
            case 2:
                OnMultLine(lin,col);
                break;
            case 3: 
                System.out.print("Block Size? ");
                blockSize = readInt();
                //OnMultBlock(lin, col, blockSize);
                break;
            }        
        } while(op!=0);
    }

    public static int readInt(){
        try {
            byte[] buffer = new byte[16];
            System.in.read(buffer);
            String input = new String(buffer).trim();
            return Integer.parseInt(input);
        } 
        catch (Exception e) {
            return 0;
        }
    }
}
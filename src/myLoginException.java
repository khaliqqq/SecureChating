/**
 * Created by KINGSMAN on 25-May-15.
 */
public class myLoginException extends Throwable {
    public myLoginException(){

    }
    public myLoginException(String msg){
        super(msg);
        System.out.println(msg);
        return;
    }
}

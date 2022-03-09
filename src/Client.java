
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Mensagem;
import util.Operacao;
import util.Status;

/**
 * @author Giovani P.
 */
public class Client {
    
    public static void trataSolicitacoes(){
        
        
    }

    public static void main(String[] args) {
        
        try {
            Socket socket = new Socket("localhost", 5555);
            
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); 
            
            Mensagem m = new Mensagem(Operacao.LOGIN);
            m.setStatus(Status.CONECTADO);
            m.setParam("nome", "123");
            m.setParam("conteudo", "123");

            System.out.println("Mensagem enviada:\n" + m);
            output.writeObject(m);
            output.flush();
            
            m = (Mensagem) input.readObject();
            
            if(m.getStatus() == Status.OK){
                if (m.getParam("res") != null) System.out.println("Res: " + m.getParam("res"));
                while(true){
                    Scanner leitor = new Scanner(System.in);
                    String op;
                    System.out.println("Operação desejada: (LOGIN, UPLOAD, DOWNLOAD, LIST, SEARCH, LOGOUT)");
                    op = leitor.nextLine();
                    
                    m = (Mensagem) input.readObject();
                }
            }else{
                System.out.println("Status: " + m.getStatus());
                if (m.getParam("res") != null) System.out.println("Res: " + m.getParam("res"));
            }

            input.close();
            output.close();
            socket.close();
            
        } catch (IOException ex) {
            System.out.println("Erro no cliente, IOE");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex){
            System.out.println("Erro no cliente, CNFE");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Mensagem;
import util.Operacao;
import util.Status;
import util.Usuario;

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
            
            Mensagem m = new Mensagem(Operacao.CADASTRO);
            
            while(true){
                Boolean flagOut = false;
                Scanner leitor = new Scanner(System.in);
                String op;
                String op2;
                System.out.println("\nOperação desejada: (LOGIN, CADASTRO, SALDO, DEPOSITO, TRANSFERENCIA, LOGOUT)");
                op = leitor.nextLine();

                switch(op.toUpperCase()) {
                    case "CADASTRO": {
                        System.out.println("Novo id de usuário: ");
                        op = leitor.nextLine();
                        System.out.println("Senha: ");
                        op2 = leitor.nextLine();

                        m.setOperacao(Operacao.CADASTRO);
                        m.setParam("id", op);
                        m.setParam("senha", op2);
                        break;
                    }
                    case "LOGIN": {
                        System.out.println("Usuário: ");
                        op = leitor.nextLine();
                        System.out.println("Senha: ");
                        op2 = leitor.nextLine();

                        m.setOperacao(Operacao.LOGIN);
                        m.setParam("user", op);
                        m.setParam("pass", op2);
                        break;
                    }
                    case "CARTEIRAS": {
                        m.setOperacao(Operacao.CARTEIRAS);
                        break;
                    }
                    case "SALDO": {
                        System.out.println("Deseja consultar seu extrato? (S/N)");
                        op = leitor.nextLine();
                        if (op.toUpperCase().equals("S")) {
                            m.setParam("extrato", true);
                        } else {
                            m.setParam("extrato", false);
                        }
                        m.setOperacao(Operacao.SALDO);
                        
                        break;
                    }
                    case "DEPOSITO": {
                        System.out.println("Informe o valor a ser depositado:");
                        op = leitor.nextLine();
                        float valor = Float.parseFloat(op);
                        if (valor <= 0) {
                            System.out.println("O valor não pode ser nulo ou menor que 0");
                            continue;
                        }
                        
                        m.setOperacao(Operacao.DEPOSITO);
                        m.setParam("valor", valor);
                        
                        break;
                    }
                    case "TRANSFERENCIA": {
                        System.out.println("Informe o usuário de destino: ");
                        op = leitor.nextLine();
                        System.out.println("Informe o valor: ");
                        op2 = leitor.nextLine();
                        
                        float valor = Float.parseFloat(op2);
                        
                        if (valor <= 0) {
                            System.out.println("O valor não pode ser nulo ou menor que 0");
                            continue;
                        }
                        
                        m.setOperacao(Operacao.TRANSFERENCIA);
                        m.setParam("id", op);
                        m.setParam("valor", valor);
                        
                        break;
                    }
                    case "RECOMPENSAR": {
                        m.setOperacao(Operacao.RECOMPENSAR);
                        break;
                    }
                    case "SAIR": {
                        m.setOperacao(Operacao.SAIR);
                        flagOut = true;
                        break;
                    }
                    case "LOGOUT": {
                        m.setOperacao(Operacao.LOGOUT);
                        break;
                    }
                    default: {
                        System.out.println("Operação inválida");
                        continue;
                    }
                }

                output.writeObject(m);
                output.flush();

                m = (Mensagem) input.readObject();


                switch(m.getOperacao()) {
                    case SALDOREPLY: {
                        float saldo = (float) m.getParam("saldo");
                        String extrato = (String) m.getParam("extrato");
                        
                        System.out.println("Seu saldo: " + saldo);
                        
                        if (extrato != null) {
                            System.out.println("Seu extrato: ");
                            System.out.println(extrato);
                        }
                    }
                    case CARTEIRASREPLY: {
                        List<Usuario> carteiras = (ArrayList<Usuario>) m.getParam("carteiras");
                        
                        if (carteiras != null) {
                            String formated = "\n------------------\n";

                            for (Usuario it: carteiras) {
                                formated += it.getId() + "\n";
                            }

                            formated += "\n------------------\n";

                            System.out.println(formated);
                        }
                    }
                    case LOGINREPLY:
                    case DEPOSITOREPLY:
                    case TRANSFERENCIAREPLY:
                    case RECOMPENSARREPLY:
                    default: {
                        if (m.getStatus() != null) {
                            System.out.println(" # Status: " + m.getStatus());
                        }
                        if (m.getParam("res") != null) {
                            System.out.println(" # Res: " + m.getParam("res"));
                        }
                        break;
                    }
                }
                
                if (flagOut) {
                    break;
                }
            }
            
            input.close();
            output.close();
            socket.close();
            
        } catch (IOException ex) {
            System.out.println("Erro no cliente, IOE");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex){
            System.out.println("Erro no cliente, CNFE");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

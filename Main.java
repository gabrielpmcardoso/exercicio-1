import java.util.Scanner;

interface Conta {
    void depositar(double valor);
    boolean sacar(double valor);
    void exibirSaldo();
}

abstract class Cliente {
    protected String nome;
    protected String tipo; 
    protected Conta[] contas;
    protected int numContas;

    public Cliente(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.contas = new Conta[10]; 
        this.numContas = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void adicionarConta(Conta conta) {
        if (numContas < contas.length) {
            contas[numContas++] = conta;
            System.out.println(" conta adicionada ");
        } else {
            System.out.println(" limite de contas ultrapassado ");
        }
    }

    public void listarContas() {
        System.out.println("contas de " + nome + ":");
        for (int i = 0; i < numContas; i++) {
            contas[i].exibirSaldo();
        }
    }
}

class PessoaFisica extends Cliente {
    public PessoaFisica(String nome) {
        super(nome, "pessoa fisica");
    }
}

class PessoaJuridica extends Cliente {
    public PessoaJuridica(String nome) {
        super(nome, "pessoa juridica");
    }
}

class ContaCorrente implements Conta {
    private double saldo;
    private double limite;

    public ContaCorrente(double limite) {
        this.saldo = 0;
        this.limite = limite;
    }

    @Override
    public void depositar(double valor) {
        saldo += valor;
        System.out.println("depositado R$ " + valor);
    }

    @Override
    public boolean sacar(double valor) {
        if (saldo + limite >= valor) {
            saldo -= valor;
            System.out.println("sacado R$ " + valor);
            return true;
        } else {
            System.out.println("saldo insuficiente");
            return false;
        }
    }

    @Override
    public void exibirSaldo() {
        System.out.println("conta corrente - saldo R$ " + saldo + " | limite R$ " + limite);
    }
}

class ContaPoupanca implements Conta {
    private double saldo;
    private double taxaRendimento;

    public ContaPoupanca(double taxaRendimento) {
        this.saldo = 0;
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public void depositar(double valor) {
        saldo += valor;
        System.out.println("depositado R$ " + valor);
    }

    @Override
    public boolean sacar(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
            System.out.println("sacado R$ " + valor);
            return true;
        } else {
            System.out.println("saldo insuficiente");
            return false;
        }
    }

    public void render() {
        saldo += saldo * taxaRendimento;
        System.out.println("rendimento aplicado");
    }

    @Override
    public void exibirSaldo() {
        System.out.println("conta poupança - saldo R$ " + saldo);
    }
}

class ContaRendimento implements Conta {
    private double saldo;
    private double taxaRendimento;

    public ContaRendimento(double taxaRendimento) {
        this.saldo = 0;
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public void depositar(double valor) {
        saldo += valor;
        System.out.println("depositado R$ " + valor);
    }

    @Override
    public boolean sacar(double valor) {
        System.out.println("conta rendimento não permite saque");
        return false;
    }

    public void render() {
        saldo += saldo * taxaRendimento;
        System.out.println("rendimento aplicado");
    }

    @Override
    public void exibirSaldo() {
        System.out.println("conta rendimento - saldo R$ " + saldo);
    }
}

class Banco {
    private Cliente[] clientes;
    private int numClientes;

    public Banco() {
        clientes = new Cliente[10]; 
        numClientes = 0;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (numClientes < clientes.length) {
            clientes[numClientes++] = cliente;
            System.out.println(cliente.getNome() + " cadastrado com sucesso");
        } else {
            System.out.println("limite de clientes excedido");
        }
    }

    public void listarClientes() {
        System.out.println("clientes no banco:");
        for (int i = 0; i < numClientes; i++) {
            System.out.println(clientes[i].getNome() + " - " + clientes[i].getTipo());
        }
    }

    public Cliente buscarCliente(String nome) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getNome().equalsIgnoreCase(nome)) {
                return clientes[i];
            }
        }
        System.out.println("cliente não encontrado");
        return null;
    }

    public void removerCliente(Cliente cliente) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i] == cliente) {
                for (int j = i; j < numClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                clientes[--numClientes] = null;
                System.out.println(cliente.getNome() + " removido do banco");
                return;
            }
        }
        System.out.println("cliente não encontrado");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Banco banco = new Banco();
        boolean running = true;

        while (running) {
            System.out.println(" sistema bancario ");
            System.out.println("1. cadastrar cliente");
            System.out.println("2. cadastrar conta para cliente");
            System.out.println("3. realizar saque");
            System.out.println("4. realizar deposito");
            System.out.println("5. listar contas de cliente");
            System.out.println("6. listar clientes");
            System.out.println("7. remover cliente");
            System.out.println("8. sair");
            System.out.print("escolha uma opçao: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    System.out.print("tipo (PF/PJ): ");
                    String tipo = scanner.nextLine();
                    if (tipo.equalsIgnoreCase("PF")) {
                        banco.cadastrarCliente(new PessoaFisica(nome));
                    } else if (tipo.equalsIgnoreCase("PJ")) {
                        banco.cadastrarCliente(new PessoaJuridica(nome));
                    } else {
                        System.out.println("tipo invalido");
                    }
                    break;
                }
                case 2: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    Cliente cliente = banco.buscarCliente(nome);
                    if (cliente != null) {
                        System.out.println("1. conta corrente");
                        System.out.println("2. conta poupança");
                        System.out.println("3. conta rendimento");
                        int tipoConta = scanner.nextInt();
                        System.out.print("taxa de rendimento ou limite: ");
                        double valor = scanner.nextDouble();

                        switch (tipoConta) {
                            case 1:
                                cliente.adicionarConta(new ContaCorrente(valor));
                                break;
                            case 2:
                                cliente.adicionarConta(new ContaPoupanca(valor));
                                break;
                            case 3:
                                cliente.adicionarConta(new ContaRendimento(valor));
                                break;
                            default:
                                System.out.println("tipo de conta invalido");
                        }
                    }
                    break;
                }
                case 3: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    Cliente cliente = banco.buscarCliente(nome);
                    if (cliente != null) {
                        cliente.listarContas();
                        System.out.print("valor do saque: ");
                        double valor = scanner.nextDouble();
                        for (int i = 0; i < cliente.numContas; i++) {
                            cliente.contas[i].sacar(valor);
                        }
                    }
                    break;
                }
                case 4: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    Cliente cliente = banco.buscarCliente(nome);
                    if (cliente != null) {
                        cliente.listarContas();
                        System.out.print("valor do deposito: ");
                        double valor = scanner.nextDouble();
                        for (int i = 0; i < cliente.numContas; i++) {
                            cliente.contas[i].depositar(valor);
                        }
                    }
                    break;
                }
                case 5: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    Cliente cliente = banco.buscarCliente(nome);
                    if (cliente != null) {
                        cliente.listarContas();
                    }
                    break;
                }
                case 6: {
                    banco.listarClientes();
                    break;
                }
                case 7: {
                    System.out.print("nome do cliente: ");
                    String nome = scanner.nextLine();
                    Cliente cliente = banco.buscarCliente(nome);
                    if (cliente != null) {
                        banco.removerCliente(cliente);
                    }
                    break;
                }
                case 8:
                    running = false;
                    System.out.println("saindo");
                    break;
                default:
                    System.out.println("opçao invalida");
            }
        }
        scanner.close();
    }
}

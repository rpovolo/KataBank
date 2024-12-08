package com.katabank.command;

public class AccountShellCommands {
/*
    private final MovementRepository movementRepository;

    private final AccountRepository accountRepository;

    public AccountShellCommands(MovementRepository movementRepository, AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
    }

    @ShellMethod("Consultar los movimientos de una cuenta.")
    public void showMovements(String cbuCvu) {

        Optional<Account> account = accountRepository.findByCbuCvu(cbuCvu);

        if (account.isEmpty()){
            System.out.println("Cuenta no encontrada.");
            return;
        }

        List<Movement> movements = movementRepository.findByAccount(cbuCvu);
        if (movements.isEmpty()) {
            System.out.println("No hay movimientos registrados para esta cuenta.");
            return;
        }

        System.out.println("Cuenta: " + cbuCvu);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-12s %-30s %-15s %-15s\n", "Fecha", "Descripción", "Monto", "Saldo");
        System.out.println("------------------------------------------------------------");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Movement movement : movements) {
            String fecha = movement.getCreatedAt().toLocalDate().format(dateFormatter);
            String descripcion = movement.getTransaction().getDescription();
            String monto = (movement.getMovementType() == MovementType.DEBIT ? "-" : "") + "$" + movement.getAmount();
            String saldo = "$" + movement.getBalanceAfter();

            System.out.printf("%-12s %-30s %-15s %-15s\n", fecha, descripcion, monto, saldo);
        }
        System.out.println("------------------------------------------------------------");
    }*/
}

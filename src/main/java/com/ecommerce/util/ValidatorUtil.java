package com.ecommerce.util;

import java.util.Scanner;

/**
 * Utilitário para realizar validações de entrada em tempo real via terminal.
 */
public class ValidatorUtil {

    /**
     * Lê uma string garantindo que não seja vazia.
     */
    public static String lerStringObrigatoria(Scanner scanner, String mensagem) {
        String input;
        while (true) {
            System.out.print(mensagem);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Entrada inválida! Este campo é obrigatório.");
        }
    }

    /**
     * Lê um email garantindo formato válido.
     */
    public static String lerEmail(Scanner scanner, String mensagem) {
        String email;
        while (true) {
            System.out.print(mensagem);
            email = scanner.nextLine().trim();
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            }
            System.out.println("Email inválido! Formato esperado: exemplo@dominio.com");
        }
    }

    /**
     * Lê um CPF validando 11 dígitos.
     */
    public static String lerCpf(Scanner scanner, String mensagem) {
        String cpf;
        while (true) {
            System.out.print(mensagem);
            cpf = scanner.nextLine().trim();
            if (cpf.matches("\\d{11}")) {
                return cpf;
            }
            System.out.println("CPF inválido! O CPF deve conter exatamente 11 números sem pontos ou traços.");
        }
    }

    /**
     * Lê um telefone garantindo que só contenha números (pelo menos 10 dígitos).
     */
    public static String lerTelefone(Scanner scanner, String mensagem) {
        String telefone;
        while (true) {
            System.out.print(mensagem);
            telefone = scanner.nextLine().trim();
            if (telefone.matches("\\d{10,11}")) {
                return telefone;
            }
            System.out.println("Telefone inválido! Formato esperado: 11999999999 (DDD + número).");
        }
    }

    /**
     * Lê um número decimal positivo (double).
     */
    public static double lerDoublePositivo(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                if (valor >= 0) {
                    return valor;
                }
                System.out.println("Valor inválido! Insira um número positivo.");
            } catch (NumberFormatException e) {
                System.out.println("Formato inválido! Insira um número válido (ex: 15.50).");
            }
        }
    }

    /**
     * Lê um número inteiro positivo.
     */
    public static int lerIntPositivo(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 0) {
                    return valor;
                }
                System.out.println("Valor inválido! Insira um número positivo.");
            } catch (NumberFormatException e) {
                System.out.println("Formato inválido! Insira um número inteiro válido.");
            }
        }
    }

    /**
     * Lê uma UF validando 2 letras maiúsculas.
     */
    public static String lerUf(Scanner scanner, String mensagem) {
        String uf;
        while (true) {
            System.out.print(mensagem);
            uf = scanner.nextLine().trim();
            if (uf.matches("^[A-Z]{2}$")) {
                return uf;
            }
            System.out.println("UF inválida! A UF deve conter exatamente 2 letras maiúsculas (ex: SP, RJ).");
        }
    }

    /**
     * Lê um nome de cidade contendo apenas letras e espaços.
     */
    public static String lerCidade(Scanner scanner, String mensagem) {
        String cidade;
        while (true) {
            System.out.print(mensagem);
            cidade = scanner.nextLine().trim();
            if (cidade.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$")) {
                return cidade;
            }
            System.out.println("Cidade inválida! A cidade deve conter apenas letras.");
        }
    }

    /**
     * Lê um CEP no formato brasileiro.
     */
    public static String lerCep(Scanner scanner, String mensagem) {
        String cep;
        while (true) {
            System.out.print(mensagem);
            cep = scanner.nextLine().trim();
            if (cep.matches("^\\d{5}-?\\d{3}$")) {
                return cep;
            }
            System.out.println("CEP inválido! O formato esperado é 12345-678 ou 12345678.");
        }
    }
}

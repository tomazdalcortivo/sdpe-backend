package br.com.ifpr.edu.sdpe_backend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;

public class UploadUtil {

    public static boolean fazerUploadImagem(MultipartFile imagem) {

        boolean sucessoUploadImagem = false;

        if(!imagem.isEmpty()){
            String nomeArquivo = imagem.getOriginalFilename();
            try {
                //Diretorio para armazenar o arquivo
                String pastaUploadImagem = "C:\\Users\\User\\IdeaProjects\\sdpe-backend\\src\\main\\resources\\static\\images\\imagesUpload";
                File dir = new File(pastaUploadImagem);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                //criar arquivo no diretorio
                File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeArquivo);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(imagem.getBytes());
                stream.close();
                System.out.println("Armazenado em: " + serverFile.getAbsolutePath());
                System.out.println("Voce fez o upload do arquivo: " + nomeArquivo);

            } catch (Exception e) {
                System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
            }
        }
        else {
            System.out.println("Erro ao carregar o arquivo. Está vazio: ");
        }
        return sucessoUploadImagem;
    }
}

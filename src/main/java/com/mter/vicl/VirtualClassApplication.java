package com.mter.vicl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class VirtualClassApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(VirtualClassApplication.class, args);
		//Path hh1 = Paths.get("src/main/resources/storage/hh1.txt");
		//File file = new File("src/main/resources/storage/hh1.txt");

		/*Path hh2 = Paths.get("src/main/resources/storage/hh2.txt");
		Path hh3 = Paths.get("src/main/resources/storage/d/hh2.txt");
		String md5 = DigestUtils.md5DigestAsHex(Files.readAllBytes(hh1));
		System.out.println(DigestUtils.md5DigestAsHex(Files.readAllBytes(hh1)));
		System.out.println(DigestUtils.md5DigestAsHex(Files.readAllBytes(hh2)));
		System.out.println(DigestUtils.md5DigestAsHex(Files.readAllBytes(hh3)));
		String str = String.format("%s/%s/%s_%s",
			md5.substring(0, 2),
			md5.substring(2, 4),
			md5,
			"hh1.txt"
		);
		System.out.println(str);*/
	}

}

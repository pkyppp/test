package com.example.demo;

import java.io.File;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile({"dev","dev2"})
@Slf4j
public class WebInitalizerForDev implements CommandLineRunner {

	@Autowired
	private PswdknrDao pswdknrDao;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;
	
	@Override
	public void run(String... args) throws Exception {
		Resource dummyData = new ClassPathResource("dummy-data.sql");
		try {
			DatabasePopulatorUtils.execute(
					new ResourceDatabasePopulator(false,false,"utf8",dummyData), dataSource);
		}catch (Exception e) {
			log.warn(e.getMessage());
		}
		log.info(ApplicationSettings.getTenantId());
		LocalDateTime time = LocalDateTimeUtil.getCurrentDateTime();
		
		Pswdknr password = new Pswdknr();
		password.setSmypgid("1234456")
		pswdknrDao.insert(password);
		
		File file = resourceLoader.getResource("classpath:/sample.pdf").getFile();
		if (file.exists()) {
			byte[] bytes = Files.readAllBytes(file.toPath());
			DefaultLobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.execute("INSERT INTO XXX (A, B, C) VALUES (?,?,?)",
					new AbstractCreatingPreparedStatementCallback(lobHandler)  {
						protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException{
							ps.setString(1,  "001");
							ps.setString(2,  "test");
							ps.setString(2,  "xxx");
							ps.setBinaryStream(3,  is, bytes.length);
						}
					});
			//........
		}
	}
}

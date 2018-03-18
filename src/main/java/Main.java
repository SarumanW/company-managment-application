import dao.DAOFactory;
import dao.dao_interface.*;
import dao.json_dao.JsonDAOFactory;
import dao.oracle_dao.OracleDAOFactory;
import dao.xml_dao.XmlDAOFactory;
import domain.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException, ParseException {
        DAOFactory daoFactory;

        daoFactory = new XmlDAOFactory();
        ManagerDAO managerDAO = daoFactory.getManagerDAO();

        Manager manager = managerDAO.findManager(1521426536);
        System.out.println(manager);

    }
}

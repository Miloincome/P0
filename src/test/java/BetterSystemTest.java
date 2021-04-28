import com.app.dao.UserDAO;
import com.app.dao.impl.UserDAOImpl;
import com.app.model.BankAccount;
import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class BetterSystemTest {
        @Test
        public void testPrintBankList(){
            User newUser = new User();
            UserDAO userDAO = new UserDAOImpl();
            try {
                List<BankAccount> bankAccountList = userDAO.viewAccounts(newUser);
                if (bankAccountList != null && bankAccountList.size() > 0) {
                    System.out.println("We have total " + bankAccountList.size() + " accounts\n");
                    for (BankAccount b : bankAccountList) {
                        System.out.println(b);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Bank account list could not be generated");
            }
        }
}


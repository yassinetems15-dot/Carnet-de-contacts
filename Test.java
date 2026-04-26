package ma.acme.contacts;

import ma.acme.contacts.dao.ContactDao;
import ma.acme.contacts.model.Contact;

public class Test {
    public static void main(String[] args) throws Exception {
        ContactDao dao = new ContactDao();
        for (Contact c : dao.findAll()) {
            System.out.printf("%-10s %-10s %s%n",
                c.getNom(), c.getPrenom(), c.getEmail());
        }
    }
}
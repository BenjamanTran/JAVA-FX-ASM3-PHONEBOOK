//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dao;

import entity.Contact;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ContactDAO {
    public ContactDAO() {
    }

    public List<Contact> loadContact(String fname) throws Exception {
        List<Contact> c = new Vector();
        LineNumberReader lnr = new LineNumberReader(new FileReader(fname));
        String line = "";

        while ((line = lnr.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] st = line.split(":");
                c.add(new Contact(st[0].trim(), st[1].trim(), st[2].trim(), st[3].trim(), st[4].trim(), st[5].trim()));
            }
        }

        lnr.close();
        return c;
    }

    public void saveToFile(List<Contact> g, String fname) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fname));
        Iterator var4 = g.iterator();

        while (var4.hasNext()) {
            Contact x = (Contact) var4.next();
            bw.write(x.toString());
        }

        bw.close();
    }

    public int indexOf(List<Contact> list, Contact g) {
        for (int i = 0; i < list.size(); ++i) {
            Contact x = (Contact) list.get(i);
            if (x.getFirstName().equalsIgnoreCase(g.getFirstName()) && x.getLastName().equalsIgnoreCase(g.getLastName())) {
                return i;
            }
        }

        return -1;
    }

    public void saveToList(List<Contact> list, Contact g) {
        list.add(g);
    }

    public void updateContact(List<Contact> list, Contact c, int i) {
        Contact x = (Contact) list.get(i);
        x.setDob(c.getDob());
        x.setEmail(c.getEmail());
        x.setFirstName(c.getFirstName());
        x.setLastName(c.getLastName());
        x.setGroup(c.getGroup());
        x.setPhone(c.getPhone());
    }

    public List<Contact> search(List<Contact> c, String group, String search) {
        if (group.equals("All")) {
            group = "";
        }

        List<Contact> ct = new Vector();
        Iterator var5 = c.iterator();

        while (var5.hasNext()) {
            Contact x = (Contact) var5.next();
            String s = x.toString().toLowerCase();
            if (s.contains(search.toLowerCase()) && x.getGroup().contains(group)) {
                ct.add(x);
            }
        }

        return ct;
    }

    public List<Contact> contactByGroup(List<Contact> c, String group) {
        if (group.equals("All")) {
            return c;
        } else {
            List<Contact> ct = new Vector();
            Iterator var4 = c.iterator();

            while (var4.hasNext()) {
                Contact x = (Contact) var4.next();
                String s = x.getGroup().toLowerCase();
                if (s.contains(group.toLowerCase())) {
                    ct.add(x);
                }
            }

            return ct;
        }
    }
}

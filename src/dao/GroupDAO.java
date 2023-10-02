//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dao;

import entity.Group;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class GroupDAO {
    public GroupDAO() {
    }

    public List<Group> loadGroup(String fname) throws Exception {
        List<Group> g = new Vector();
        LineNumberReader lnr = new LineNumberReader(new FileReader(fname));
        String line = "";

        while((line = lnr.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                g.add(new Group(line));
            }
        }

        lnr.close();
        return g;
    }

    public void saveGroupToFile(List<Group> g, String fname) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fname));
        Iterator var4 = g.iterator();

        while(var4.hasNext()) {
            Group x = (Group)var4.next();
            bw.write(x.toString());
        }

        bw.close();
    }

    public int indexOf(List<Group> list, Group g) {
        for(int i = 0; i < list.size(); ++i) {
            Group x = (Group)list.get(i);
            if (x.getName().equalsIgnoreCase(g.getName())) {
                return i;
            }
        }

        return -1;
    }

    public void saveGroupToList(List<Group> list, Group g) {
        list.add(g);
    }

    public List<Group> search(List<Group> c, String search) {
        List<Group> ct = new Vector();
        Iterator var4 = c.iterator();

        while(var4.hasNext()) {
            Group x = (Group)var4.next();
            String s = x.toString().toLowerCase();
            if (s.contains(search.toLowerCase())) {
                ct.add(x);
            }
        }

        return ct;
    }

    public boolean updateGroup(List<Group> groups, Group newGroup) {
        int c = 0;
        Iterator var4 = groups.iterator();

        while(var4.hasNext()) {
            Group x = (Group)var4.next();
            if (x.getName().equalsIgnoreCase(newGroup.getName())) {
                ++c;
            }
        }

        if (c >= 2) {
            return false;
        } else {
            Group g = (Group)groups.get(this.indexOf(groups, newGroup));
            g.setName(newGroup.getName());
            return true;
        }
    }
}

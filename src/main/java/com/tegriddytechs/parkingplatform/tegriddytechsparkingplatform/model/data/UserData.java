package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.UserXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserData extends UserXmlRepository {

    private final ArrayList<User> users = new ArrayList<>();

    public UserData() throws IOException, JDOMException {
        super();
        reload();
    }

    public void reload() {
        users.clear();
        users.addAll(super.findAll()); // XML -> cache
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }



    public User findByIdFast(int userId) {
        for (User u : users) {
            if (u.getId() == userId) return u;
        }
        return null;
    }

    public User findByUsername(String username) {
        if (username == null) return null;
        for (User u : users) {
            if (u.getUserName() != null && u.getUserName().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> findByRole(UserRole role) {
        ArrayList<User> usersToReturn = new ArrayList<>();
        if (role == null) return usersToReturn;

        for (User u : users) {
            if (role.equals(u.getUserRole())) {
                usersToReturn.add(u);
            }
        }
        return usersToReturn;
    }

    public void replaceAll(List<User> newUsers) throws IOException {
        // Si quieres que "replaceAll" también se refleje en XML,
        // se necesita una estrategia de sync (borrado+update).
        // Por ahora: recargar y luego update de cada uno.
        users.clear();
        if (newUsers != null) users.addAll(newUsers);

        for (User u : users) {
            super.update(u); // XML primero por cada elemento
        }
    }

    @Override
    public List<User> findAll() {
        return users; // cache
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(findByIdFast(id)); // cache
    }

    @Override
    public void insert(User user) throws IOException {
        if (user == null) throw new IllegalArgumentException("user cannot be null");
        User existing = findByIdFast(user.getId());
        if (existing == null) {
            super.insert(user);
            users.add(user);
        } else {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists");
        }
    }

    @Override
    public void update(User user) throws IOException {
        if (user == null) throw new IllegalArgumentException("user cannot be null");
        super.update(user);
        User existing = findByIdFast(user.getId());
        if (existing != null) users.remove(existing);
        users.add(user);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        boolean deleted = super.deleteById(id); // XML primero
        if (!deleted) return false;

        User existing = findByIdFast(id);
        if (existing != null) users.remove(existing);
        return true;
    }

    @Override
    public boolean delete(User user) throws IOException {
        if (user == null) return false;
        return deleteById(user.getId());
    }

//Para controller
    public void addUser(User user) throws IOException {
        this.insert(user);
    }

    public void deleteUser(User user) throws IOException {
        delete(user);
    }

    public void updateUser(User user) throws IOException {
        this.update(user);
    }

    private int getNextUserIDByCount() {
        int count = 0;
        for (User u : users) {
            count++;
        }
        return count + 1;
    }

    public int getNextClerkIDByCount() {
        return getNextUserIDByCount();
    }

    public int getNextAdminIDByCount() {
        return getNextUserIDByCount();
    }

    public List<Clerk> getClerks() {
        List<Clerk> clerks = new ArrayList<>();
        for (User u : users) {
            if (u == null) continue;

            if (u instanceof Clerk clerk) {
                clerks.add(clerk);
                continue;
            }

            // Fallback
            if (u.getUserRole() == UserRole.CLERK) {
                Clerk clerk = new Clerk(u.getId(), u.getName(), u.getUserName(), u.getPassword(), null);
                clerks.add(clerk);
            }
        }
        return clerks;
    }

    public List<Administrator> getAdmins() {
        List<Administrator> administrators = new ArrayList<>();
        for (User u : users) {
            if (u == null) continue;

            if (u instanceof Administrator admin) {
                administrators.add(admin);
                continue;
            }

            if (u.getUserRole() == UserRole.ADMIN) {
                Administrator admin = new Administrator(u.getId(), u.getName(), u.getUserName(), u.getPassword(), new ArrayList<>());
                administrators.add(admin);
            }
        }
        return administrators;
    }
}

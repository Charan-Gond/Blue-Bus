package com.bluebus.repository;

import com.bluebus.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class Repo {
         Connection conn;

        @Autowired
        Environment e;

         @PostConstruct
    public  void load() {
             try
             {
                String url=e.getProperty("url");
                String user= e.getProperty("user");
                String password=e.getProperty("password");
                 conn=DriverManager.getConnection(Objects.requireNonNull(
                         url),user,
                         password);
                 System.out.println("connected");
             } catch (SQLException e) {
                e.printStackTrace();
             }

         }



    public String adduser(String name,String email,String password,String phoneNumber){
        try {
            var ps=conn.prepareStatement("insert into `blue_bus`.`user`(`username`,`pass`,`phoneNumber`,`email`) values(?,?,?,?);");
            ps.setString(1,name);
            ps.setString(4,email);
            ps.setString(2,password);
            ps.setString(3,phoneNumber);
            int x= ps.executeUpdate();
           if(x>0)
               return "user is added";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "user is not added";
    }

    public User login(Login l) {
             PreparedStatement s;
        try {
            s=conn.prepareStatement ("SELECT * FROM blue_bus.user WHERE username = ? AND pass = ?");
            s.setString(1,l.getUsername());
            s.setString(2,l.getPassword());
          ResultSet x=  s.executeQuery();
         while( x.next()){
             User u=new User();
             u.setUid(x.getInt(1));
             u.setName(l.getUsername());
             u.setEmail(x.getString("email"));
             u.setPassword(l.getPassword());
             u.setPhoneNumber(Long.parseLong(x.getString(4)));
             return u;
         }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String addTicket(int uid, int bus_id, String td,int n)   {
        PreparedStatement ps= null;
        int payId=0;
        try{
           payId=addPayment();
            ps = conn.prepareStatement("INSERT INTO `blue_bus`.`tickets` (`user_id`, `payment_id`, `bus_id`, `seat_no`, " +
                    "`travelling_date`, `Booked_date`) \n" +
                    "                    VALUES (?,?,?,?,?,?);");
            ps.setInt(1,uid);
            ps.setInt(2,payId);
            ps.setInt(3,bus_id);
            ps.setInt(4,n);
            ps.setDate(5, (Date.valueOf(td)));
            ps.setDate(6, Date.valueOf((LocalDate.now())));
        int x=ps.executeUpdate();
        if(x>0)
            return "ticket is booked";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "not booked";
    }
    public List<Ticket> getTicket(int id){
             List<Ticket> ts=new ArrayList<>();
        PreparedStatement ps= null;
        try {
            ps = conn.prepareStatement("select * from blue_bus.tickets where user_id=?;");
            ps.setInt(1,id);
            ResultSet r=ps.executeQuery();
            while(r.next()){
                Ticket t=new Ticket(r.getInt(1),r.getInt(2),r.getInt(3),r.getInt(4),
                        r.getInt(5),r.getDate(6),r.getDate(7));
                ts.add(t);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
             return ts;
    }

    public List<Bus> getBus(String s, String d) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "SELECT DISTINCT (r.route_id) " +
                            "                    FROM blue_bus.route_station_mapping sr1\n" +
                            "                    JOIN blue_bus.station s1 ON sr1.station_id = s1.station_id\n" +
                            "                    JOIN blue_bus.route_station_mapping sr2 ON sr1.route_id = sr2.route_id\n" +
                            "                    JOIN blue_bus.station s2 ON sr2.station_id = s2.station_id\n" +
                            "                    JOIN blue_bus.route r ON sr1.route_id = r.route_id\n" +
                            "                    WHERE s1.station_name = ?\n" +
                            "                    AND s2.station_name = ?;"
            );

            ps.setString(1, s);
            ps.setString(2, d);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                try {
                  return  getAllBus(r.getInt(1));
                } catch(SQLException e){
                }
              /*  PreparedStatement pa;
                pa=conn.prepareStatement("select s.station_name from  station s " +
                        "JOIN route_station_mapping m ON m.station_id=s.station_id where m.station_id=? order by m.created_at");
               pa.setInt(1,r.getInt(1));
               ResultSet e=ps.executeQuery();

               while(e.next()){
                 x.add(e.getString(1));
               }
                t.setStations(x);
               l.add(t);
            }  */
            }

        }catch(SQLException e){
        }
        return null;
         }

    public int addPayment() {
        PreparedStatement ps;
        try {
                ps = conn.prepareStatement("INSERT INTO `blue_bus`.`payment` (`booked_on`, `amount`) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf((LocalDate.now())));
            ps.setFloat(2, 500f);
            int z = ps.executeUpdate();
            if (z > 0) {
                ResultSet pr = ps.getGeneratedKeys();
                pr.next();
                return pr.getInt(1);

            }
        }catch(SQLException e){
                    throw new RuntimeException(e);
                }
            return 0;
        }
    public List<Route> getAllRoute(){
        Statement ps = null;
        List<Route> l=new ArrayList<>();
        try {
         ps= conn.createStatement();
        ResultSet r= ps.executeQuery("select * from  blue_bus.payments");
         while(r.next()){
             Route x=new Route(r.getInt("roueId"), r.getString(2),r.getString(3),
                     r.getString(4),r.getFloat(5));
             l.add(x);
        }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l;

    }
    public List<Bus> getAllBus(){
        Statement ps = null;
        List<Bus> l=new ArrayList<>();
        try {
            ps= conn.createStatement();
            ResultSet r= ps.executeQuery("select * from  blue_bus.bus");
            while(r.next()){
                Bus x=new Bus(r.getInt(1),r.getString(2),r.getInt(3),
                        r.getInt(4),r.getTime(5),
                        r.getTime(6));
                l.add(x);
            }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l;

    }

    public List<Bus> getAllBus(int rout_id){
        PreparedStatement ps = null;
        List<Bus> l=new ArrayList<>();
        try {
            ps= conn.prepareStatement("select * from  blue_bus.bus where route_id=?");
            ps.setInt(1,rout_id);
            ResultSet r= ps.executeQuery();
            while(r.next()){
                Bus x=new Bus(r.getInt(1),r.getString(2),r.getInt(3),
                        r.getInt(4),r.getTime(5),
                        r.getTime(6));
                l.add(x);
            }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l;

    }



}

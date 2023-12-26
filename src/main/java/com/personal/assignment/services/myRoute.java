package com.personal.assignment.services;

import com.personal.assignment.model.users;
import com.personal.assignment.passwors.generator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class myRoute extends RouteBuilder {


    @Autowired
    generator generator;

    @Autowired
    DataSource dataSource;


    public DataSource getDataSource(){
        return dataSource;
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource=dataSource;
    }




//    final DataFormat bindy = new BindyCsvDataFormat(users.class);

    @Override
    public void configure() throws Exception {

        from("direct:insert").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                users uer = exchange.getIn().getBody(users.class);
                if(uer.getUserPassword()==null) {
                    uer.setUserPassword(generator.generatePass());
                }
                String qry="INSERT INTO users(id,userName,userPassword,isDeleted)values('" + uer.getId()+ "','"
                        + uer.getUserName() +"','"+uer.getUserPassword()+"','"+uer.getIsDeleted()+"')";

//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("id", uer.getId());
//                parameters.put("userName", uer.getUserName());
//                parameters.put("password", uer.getPassword());
//                parameters.put("isDeleted", uer.getIsDeleted());

                exchange.getIn().setBody(qry);
            }

        }).to("jdbc:dataSource").end();


        // soft delete query
        from("direct:delete").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String userIdToUpdate = exchange.getIn().getBody(String.class);
                String qry="UPDATE users SET isDeleted = true WHERE id = '" + userIdToUpdate + "'";

                exchange.getIn().setBody(qry);
            }
        }).to("jdbc:dataSource")
                .end();

        //read data
        from("direct:select").setBody(constant("select * from users where IsDeleted=false")).to("jdbc:dataSource")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        ArrayList<Map<String,String>>dataLst=(ArrayList<Map<String,String>>)exchange.getIn().getBody();
                        List<users> usrs=new ArrayList<users>();
                        for (Map<String,String> data:dataLst){
                            users usr=new users();
                            usr.setId(data.get("id"));
                            usr.setUserName(data.get("UserName"));
                            usr.setUserPassword(data.get("UserPassword"));
                            usr.setIsDeleted(data.get("IsDeleted"));

                            usrs.add(usr);
                        }
                        exchange.getIn().setBody(usrs);
                    }
                }).end();



        //get one user
        from("direct:getone").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String username = exchange.getIn().getBody(String.class);
                String qry="select user from users where userName='"+username+"'";
                exchange.getIn().setBody(qry);
            }
        }).to("jdbc:dataSource").end();


//        from("file:resources/sample.csv").unmarshal().csv()
//                .split(body())
//                .to("direct:processcsvRow");
//
//        from("direct:processcsvRow")
//                .setBody(simple("INSERT INTO users (id, userName,userPassword, isDeleted) VALUES (${body[0]}, '${body[1]}','${body[2]}', ${body[3]})"))
//                .to("jdbc:dataSource");

    }
}

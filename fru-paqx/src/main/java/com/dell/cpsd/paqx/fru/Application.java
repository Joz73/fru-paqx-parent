/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru;

import com.dell.cpsd.hdp.capability.registry.client.lookup.config.CapabilityRegistryLookupManagerConfig;
import org.h2.tools.Server;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import java.sql.SQLException;

/**
 * Startup application.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@SpringBootApplication
@Import({CapabilityRegistryLookupManagerConfig.class})
public class Application extends SpringBootServletInitializer
{

    public static void main(String[] args)
    {
        new Application().configure(new SpringApplicationBuilder(Application.class)).bannerMode(Banner.Mode.LOG).run(args);
        try
        {
            System.out.println("WE ARE HERE!!");
            Server webServer = Server.createWebServer("-web","-webAllowOthers","-webPort","8085").start();
            Server server = Server.createTcpServer().start();//("-web","-webAllowOthers","-webPort","9092").start();
            //Server webServer = Server.createWebServer("-web,-trace").start();
            //Server server = Server.createTcpServer("-tcp,-tcpAllowOthers,true,-tcpPort,9092").start();
            //Server server=Server.create
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
    }
}

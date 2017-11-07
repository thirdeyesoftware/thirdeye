package com.iobeam.portal.security.rdbms.authenticator;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import weblogic.management.utils.NotFoundException;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;
import weblogic.security.spi.WLSGroup;
import weblogic.security.spi.WLSUser;
import java.util.logging.Logger;

public final class PortalLoginModule implements LoginModule {
   private CallbackHandler callbackHandler;
   private static Logger cLogger =
		 Logger.getLogger("com.iobeam.portal.security.rdbms.authenticator");

   // Determine whether this is a login or assert identity
   private boolean isIdentityAssertion;

   // Authentication status
   private boolean loginSucceeded;
   private Vector principalsForSubject = new Vector();
   private boolean principalsInSubject;
   private Subject subject;


   /**
    *  <i>Method not documented.</i>
    *
    *@return                     description of the Returned Value.
    *@exception  LoginException  if the <description>.
    */
   public boolean abort()
      throws LoginException {
      // only called (once!) after login or commit
      // or may be? called (n times) after abort

      // loginSucceeded should be true or false
      // user should be null if !loginSucceeded, otherwise null or not-null
      // group should be null if user == null,   otherwise null or not-null
      // principalsInSubject should be false if user is null, otherwise true or false

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "PortalLoginModule.abort" );
      }

      if ( principalsInSubject ) {
         subject.getPrincipals().removeAll( principalsForSubject );
         principalsInSubject = false;
      }


      return true;
   }


   /**
    *  <i>Method not documented.</i>
    *
    *@return                     description of the Returned Value.
    *@exception  LoginException  if the <description>.
    */
   public boolean commit()
      throws LoginException {
      // only called (once!) after login

      // loginSucceeded      should be true or false
      // principalsInSubject should be false
      // user  should be null if !loginSucceeded, null or not-null otherwise
      // group should be null if user == null,    null or not-null otherwise

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "PortalLoginModule.commit" );
      }

      if ( loginSucceeded ) {
         subject.getPrincipals().addAll( principalsForSubject );
         principalsInSubject = true;
         return true;
      } else {
         return false;
      }
   }


   /**
    *  <i>Method not documented.</i>
    *
    *@param  subject          Description of Parameter.
    *@param  callbackHandler  Description of Parameter.
    *@param  sharedState      Description of Parameter.
    *@param  options          Description of Parameter.
    */
   public void initialize(
         Subject subject,
         CallbackHandler callbackHandler,
         Map sharedState,
         Map options
          )
   {
      // only called (once!) after the constructor and before login

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "PortalLoginModuleImpl.initialize" );
      }

      this.subject = subject;
      this.callbackHandler = callbackHandler;

      // Check for Identity Assertion option, this should not be on by default
      isIdentityAssertion =
            "true".equalsIgnoreCase( ( String ) options.get( "IdentityAssertion" ) );

   }


   /**
    *  <i>Method not documented.</i>
    *
    *@return                     description of the Returned Value.
    *@exception  LoginException  if the <description>.
    */
   public boolean login()
      throws LoginException
   {
      // only called (once!) after initialize

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "PortalLoginModuleImpl.login" );
      }

      // loginSucceeded      should be false
      // principalsInSubject should be false
      // user                should be null
      // group               should be null

      Callback[] callbacks = getCallbacks();

      String userName = getUserName( callbacks );

      if ( userName.length() > 0 ) {
         if ( PortalAuthenticatorDAO.userExists( userName ) == false ) {
            throwFailedLoginException( "Authentication Failed: User " + userName + " doesn't exist." );
         }
         if ( !isIdentityAssertion ) {
            String passwordWant = null;
            try {
               String passwordHave = getPasswordHave( userName, callbacks );
               if ( !PortalAuthenticatorDAO.authenticate( userName, passwordHave ) ) {
                  throwFailedLoginException(
                        "Authentication Failed: User " + userName + " bad password.  ");
               }
            } catch ( NotFoundException shouldNotHappen ) {
            }

         }
      } else {
         // anonymous login - let it through?

         // Print debugging information
         if ( 1 == 1 ) {
            cLogger.info( "\tempty userName" );
         }
      }

      loginSucceeded = true;
      principalsForSubject.add( new WLSUserImpl( userName ) );
      addGroupsForSubject( userName );

      return loginSucceeded;
   }


   /**
    *  <i>Method not documented.</i>
    *
    *@return                     description of the Returned Value.
    *@exception  LoginException  if the <description>.
    */
   public boolean logout()
      throws LoginException
   {
      // should never be called

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "PortalLoginModule.logout" );
      }

      return true;
   }


   /**
    *  Assigns a GroupsForSubject to the PortalLoginModuleImpl.
    *
    *@param  userName  a GroupsForSubject to be assigned.
    */
   private void addGroupsForSubject( String userName ) {
      for ( Enumeration e = PortalAuthenticatorDAO.selectUserGroups( userName ); e.hasMoreElements();  ) {
         String groupName = ( String ) e.nextElement();

         // Print debugging information
         if ( 1 == 1 ) {
            cLogger.info( "\tgroupName\t= " + groupName );
         }

         principalsForSubject.add( new WLSGroupImpl( groupName ) );
      }
   }


   /**
    *  Returns the Callbacks property of the PortalLoginModuleImpl.
    *
    *@return                     The Callbacks value.
    *@exception  LoginException  if the <description>.
    */
   private Callback[] getCallbacks()
      throws LoginException
   {
      if ( callbackHandler == null ) {
         throwLoginException( "No CallbackHandler Specified" );
      }

      Callback[] callbacks;
      if ( isIdentityAssertion ) {
         callbacks = new Callback[1];
      } else {
         callbacks = new Callback[2];
         callbacks[1] = new PasswordCallback( "password: ", false );
      }
      callbacks[0] = new NameCallback( "username: " );

      try {
         callbackHandler.handle( callbacks );
      } catch ( IOException e ) {
         throw new LoginException( e.toString() );
      } catch ( UnsupportedCallbackException e ) {
         throwLoginException( e.toString() + " " + e.getCallback().toString() );
      }

      return callbacks;
   }


   /**
    *  Returns the PasswordHave property of the PortalLoginModuleImpl.
    *
    *@param  userName            Description of Parameter.
    *@param  callbacks           Description of Parameter.
    *@return                     The PasswordHave value.
    *@exception  LoginException  if the <description>.
    */
   private String getPasswordHave( String userName, Callback[] callbacks )
      throws LoginException
   {
      PasswordCallback passwordCallback = ( PasswordCallback ) callbacks[1];
      char[] password = passwordCallback.getPassword();
      passwordCallback.clearPassword();
      if ( password == null || password.length < 1 ) {
         throwLoginException( "Authentication Failed: User " + userName + ".  Password not supplied" );
      }
      String passwd = new String( password );

      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "\tpasswordHave\t= " + passwd );
      }

      return passwd;
   }


   /**
    *  Returns the UserName property of the PortalLoginModuleImpl.
    *
    *@param  callbacks           Description of Parameter.
    *@return                     The UserName value.
    *@exception  LoginException  if the <description>.
    */
   private String getUserName( Callback[] callbacks )
      throws LoginException
   {
      String userName = ( ( NameCallback ) callbacks[0] ).getName();
      if ( userName == null ) {
         throwLoginException( "Username not supplied." );
      }
      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "\tuserName\t= " + userName );
      }

      return userName;
   }


   /**
    *  <i>Method not documented.</i>
    *
    *@param  msg                       Description of Parameter.
    *@exception  FailedLoginException  if the <description>.
    */
   private void throwFailedLoginException( String msg )
      throws FailedLoginException
   {
      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "Throwing FailedLoginException(" + msg + ")" );
      }

      throw new FailedLoginException( msg );
   }


   /**
    *  <i>Method not documented.</i>
    *
    *@param  msg                 Description of Parameter.
    *@exception  LoginException  if the <description>.
    */
   private void throwLoginException( String msg )
      throws LoginException
   {
      // Print debugging information
      if ( 1 == 1 ) {
         cLogger.info( "Throwing LoginException(" + msg + ")" );
      }

      throw new LoginException( msg );
   }
}

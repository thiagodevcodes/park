import NextAuth, { NextAuthOptions } from "next-auth"
import CredentialsProvider from "next-auth/providers/credentials"
import axios from "axios"


const baseUrl = "http://localhost:8080"

const nextAuthOptions: NextAuthOptions = {
    providers: [
        CredentialsProvider({
            name: 'credentials',
            credentials: {
                email: { label: 'email', type: 'text' },
                password: { label: 'password', type: 'password' }
            },

            async authorize(credentials, req) {
                const response = await axios({
                    method: 'POST',
                    url: baseUrl + '/api/login',
                    headers: { "Content-Type": "application/json" }, 
                    data: {
                        username: credentials?.email,
                        password: credentials?.password
                    }
                })

                const user = await response.data
                if(user && response.status == 200) {
                    return user
                }

                return null
            }
        })
    ],

    pages: {
        signIn: '/'
    },

    session: {
        strategy: "jwt",
        maxAge: 2 * 60 * 60,
    },
    
    callbacks: {
        async jwt({ token, user }) {
            if (user) {
                token.user = user;
            }
            return token;
        },

        async session({ session, token }) {

            if (token.user) {
                session.user = token.user;
            }
            return session;
        }
    }
}

export default nextAuthOptions
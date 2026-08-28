import { signInWithRedirect, signOut } from "aws-amplify/auth";

export async function login() {
  await signInWithRedirect();
}

export async function logout() {
  await signOut();
}

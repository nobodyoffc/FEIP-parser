package tools;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jcajce.provider.digest.Keccak;


public class Hash {
	
    private Hash() { }
	
	public static byte[] Sha256(byte[] b) {
		return Hashing.sha256().hashBytes(b).asBytes();
	}
	
	public static byte[] Sha256x2(byte[] b) {
		return Hashing.sha256().hashBytes(Hashing.sha256().hashBytes(b).asBytes()).asBytes();
	}
	
	public static byte[] Sha512x2(byte[] b) {
		return Hashing.sha512().hashBytes(Hashing.sha512().hashBytes(b).asBytes()).asBytes();
	}
	
	public static String Sha256(String s) {
		return Hashing.sha256().hashBytes(s.getBytes()).toString();
	}
	
	public static String Sha256x2(String s) {
		return Hashing.sha256().hashBytes(Hashing.sha256().hashBytes(s.getBytes()).asBytes()).toString();
	}
	
	public static String Sha512x2(String s) {
		return Hashing.sha512().hashBytes(Hashing.sha512().hashBytes(s.getBytes()).asBytes()).toString();
	}
	
    /**
     * Keccak-256 hash function.
     *
     * @param input binary encoded input data
     * @param offset of start of data
     * @param length of data
     * @return hash value
     */
    public static byte[] sha3(byte[] input, int offset, int length) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(input, offset, length);
        return kecc.digest();
    }

    /**
     * Keccak-256 hash function.
     *
     * @param input binary encoded input data
     * @return hash value
     */
    public static byte[] sha3(byte[] input) {
        return sha3(input, 0, input.length);
    }


    /**
     * Generates SHA-256 digest for the given {@code input}.
     *
     * @param input The input to digest
     * @return The hash value for the given input
     * @throws RuntimeException If we couldn't find any SHA-256 provider
     */
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't find a SHA-256 provider", e);
        }
    }

    public static byte[] hmacSha512(byte[] key, byte[] input) {
        HMac hMac = new HMac(new SHA512Digest());
        hMac.init(new KeyParameter(key));
        hMac.update(input, 0, input.length);
        byte[] out = new byte[64];
        hMac.doFinal(out, 0);
        return out;
    }

    public static byte[] sha256hash160(byte[] input) {
        byte[] sha256 = sha256(input);
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256, 0, sha256.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        return out;
    }
}

	
	
	


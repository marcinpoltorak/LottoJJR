package pl.lotto.numberreceiver;

public class HashGeneratorTestImpl implements HashGenerable{

    String hash;

    public HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    public HashGeneratorTestImpl() {
        hash = "123";
    }

    @Override
    public String getHash() {
        return hash;
    }
}

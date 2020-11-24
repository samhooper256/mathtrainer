package suppliers.sequences;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.BigInteger;

import math.*;
import problems.*;
import suppliers.*;

/**
 * Supplies {@link Sequence} {@link Problem Problems} for the sequences of {@link SpecialSequences.Squares Squares},
 * {@link SpecialSequences.Cubes Cubes}, and {@link SpecialSequences.Triangles Triangular Numbers}.
 * @author Sam Hooper
 *
 */
public class OtherSequencesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SEQ_LENGTH = RangeStore.of(2, 10, 4, 8);
	
	private final NamedIntRange seqLength = of(SEQ_LENGTH, "Length of sequence");
	
	public OtherSequencesSupplier() {
		settings(seqLength);
	}

	@Override
	public Problem get() {
		int len = intInclusive(seqLength);
		SummableSequence<BigInteger> seq = randSeq().subSequence(1, len);
		return ComplexValued.of(String.format("What is the sum of the first %d terms of the sequence %s?", len, seq.toPartialString(4)), new Complex(seq.sum()));
		
	}
	
	private static SummableSequence<BigInteger> randSeq() {
		int ran = RAND.nextInt(3);
		return ran == 1 ? SpecialSequences.Squares.INFINITE : ran == 2 ? SpecialSequences.Cubes.INFINITE : SpecialSequences.Triangles.INFINITE;
	}
	
}

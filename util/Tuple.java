package com.lamp.commons.lang.util;

public class Tuple {

	public static class Unit< A > {
		private final A unit;

		public Unit( A unit ) {
			super( );
			this.unit = unit;
		}

		public A getUnit( ) {
			return unit;
		}

		@Override
		public String toString( ) {
			return "Unit [unit=" + unit + "]";
		}
		
		
	}

	public static class Pair< A , B > extends Unit< A > {
		private final B pair;

		public Pair( A unit , B pair ) {
			super( unit );
			this.pair = pair;
		}

		public B getPair( ) {
			return pair;
		}

		@Override
		public String toString( ) {
			return "Pair [pair=" + pair + "unit=" + getUnit( ) + "]";
		}
		
		
		
	}

	public static class Triplet<A , B , C> extends Pair<A , B>{
		private final C triplet;

		public Triplet( A unit , B pair , C triplet ) {
			super( unit , pair );
			this.triplet = triplet;
		}

		public C getTriplet( ) {
			return triplet;
		}
		
		
	}

	public static class Quartet<A,B,C,D> extends Triplet<A , B , C>{
		private final D quartet;

		public Quartet( A unit , B pair , C triplet , D quartet ) {
			super( unit , pair , triplet );
			this.quartet = quartet;
		}

		public D getQuartet( ) {
			return quartet;
		}
		
		
	}
	
	public static class Quintet<A,B,C,D,E> extends Quartet<A,B,C,D>{
		private final E quintet;

		public Quintet( A unit , B pair , C triplet , D quartet , E quintet ) {
			super( unit , pair , triplet , quartet );
			this.quintet = quintet;
		}

		public final E getQuintet( ) {
			return quintet;
		}
		
		
	}
	static class Sextet<A,B,C,D,E,F> extends Quintet<A,B,C,D,E>{
		private F sextet;	
		
		public Sextet( A unit , B pair , C triplet , D quartet , E quintet , F sextet ) {
			super( unit , pair , triplet , quartet , quintet );
			this.sextet = sextet;
		}



		public final F getSextet( ) {
			return sextet;
		}

		
	}
	static class Septet<A,B,C,D,E,F,G> extends Sextet<A,B,C,D,E,F>{
		private G septet;

		

		public Septet( A unit , B pair , C triplet , D quartet , E quintet , F sextet , G septet ) {
			super( unit , pair , triplet , quartet , quintet , sextet );
			this.septet = septet;
		}



		public final G getSeptet( ) {
			return septet;
		}

		
		
		
		
	}
	static class Octet<A,B,C,D,E,F,G,H> extends Septet<A,B,C,D,E,F,G>{
		private final H octet;

		public Octet( A unit , B pair , C triplet , D quartet , E quintet , F sextet , G septet , H octet ) {
			super( unit , pair , triplet , quartet , quintet , sextet , septet );
			this.octet = octet;
		}

		public H getOctet( ) {
			return octet;
		}
		
		
	}
	static class Ennead<A,B,C,D,E,F,G,H,I> extends Octet<A,B,C,D,E,F,G,H>{
		private final I ennead;

		public Ennead( A unit , B pair , C triplet , D quartet , E quintet , F sextet , G septet , H octet ,
				I ennead ) {
			super( unit , pair , triplet , quartet , quintet , sextet , septet , octet );
			this.ennead = ennead;
		}

		public I getEnnead( ) {
			return ennead;
		}

		
		
		
	}
	static class Decade<A,B,C,D,E,F,G,H,I,J> extends Ennead<A,B,C,D,E,F,G,H,I>{
		private final J decade;

		
		
		public Decade( A unit , B pair , C triplet , D quartet , E quintet , F sextet , G septet , H octet , I ennead ,
				J decade ) {
			super( unit , pair , triplet , quartet , quintet , sextet , septet , octet , ennead );
			this.decade = decade;
		}



		public J getDecade( ) {
			return decade;
		}
		
		
	}
}

package tsxdk.io.query;

import java.util.LinkedList;
import java.util.List;

import system.core.Context;
import api.data.TsEntityType;
import api.query.AbstractQuery;
import api.query.QueryBuilder;
import api.query.LibTsCmd;
import api.query.LibTsParm;

public final class BuildQuery implements QueryBuilder {
	
	private final LibTsCmd command;
	private List<TsArgument> args;
	private String argRepetition;
	private boolean preparable;
	private TsEntityType expectedReturn = TsEntityType.TSRETURN;
	
	public BuildQuery (LibTsCmd cmd) {
		this.args = null;
		this.argRepetition = null;
		this.command = cmd;
	}
	
	@Override
	public QueryBuilder With(LibTsParm parm) {
		return With(parm, null);
	}
	
	@Override
	public QueryBuilder With(LibTsParm parm, Object value) {
		if (args == null) {
			args = new LinkedList<>();
		}
		args.add(new TsArgument(parm,value));
		return this;
	}
	
	@Override
	public QueryBuilder WithChained(LibTsParm parm, Object[] values) {
		if (argRepetition != null) {
			throw new UnsupportedOperationException("Only one argument-chain per command allowed");
		}
		
		if (preparable) {
			throw new IllegalStateException("A preparable query can not have chained arguments");
		}

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.length; ++i) {
			builder.append(parm.getValue());
			builder.append("=");
			builder.append(values[i].toString());
			if (i != values.length - 1)
				builder.append("|");
		}

		argRepetition = builder.toString();

		return this;
	}
	
	@Override
	public QueryBuilder Expecting(TsEntityType type) {
		expectedReturn = type;
		return this;
	}
	
	@Override
	public AbstractQuery Publish() {
		if (command == null) {
			throw new NullPointerException("No command set for BuildQuery!");
		}
		
		StringBuilder query = new StringBuilder(command.getValue());
		
		if (argRepetition != null) {
			query.append(" ");
			query.append(argRepetition);
			query.append(" ");
		}

		if (args != null) {
			for (TsArgument arg : args) {
				String parmArg = null;
				if (arg.value != null) {
					if (!preparable) {
						parmArg = arg.value.toString();
					} else {
						parmArg = "%s";
					}
				}

				query.append(" ");
				
				if (parmArg == null)
					query.append("-" + arg.parameter.getValue());
				else {
					query.append(arg.parameter.getValue());
					query.append("=");
					query.append(parmArg);
				}
			}
			args.clear();
		}
		
		if (!preparable)
			return Context.getQueryAgent().prepareQuery(query.toString(), expectedReturn);
		else
			return Context.getQueryAgent().prepareFormattableQuery(query.toString(), expectedReturn);
	}
	
	private static final class TsArgument {
		LibTsParm parameter;
		Object value;
		
		TsArgument (LibTsParm parameter, Object value) {
			this.parameter = parameter;
			this.value = value;
		}
	}

	@Override
	public QueryBuilder Preparable() {
		if (argRepetition != null) {
			throw new IllegalStateException("A preparable query can not have chained arguments");
		}
		preparable = true;
		return this;
	}
}

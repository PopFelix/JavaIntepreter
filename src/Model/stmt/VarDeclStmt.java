package Model.stmt;


import Exceptions.InterpreterError;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;
import Model.value.IValue;

public class VarDeclStmt implements IStmt{
    String name;
    IType type;

    public VarDeclStmt(String name, IType type){
        this.name = name;
        this.type = type;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        IDict<String,IValue> symTable=state.getSymTable();
        if (!symTable.isDefined(name)){
            symTable.add(name,type.defaultValue());
            return null;
        }
        throw new InterpreterError(String.format("Variable %s already declared",name));

    }

    @Override
    public String toString() {
        return String.format("%s %s;",type.toString(),name);
    }
}
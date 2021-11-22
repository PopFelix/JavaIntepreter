package Model.stmt;

import Exceptions.*;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class HeapWrite implements IStmt{
    String varName;
    Exp exp;

    public HeapWrite(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError, DictError, VarNotDefinedError, InvalidTypeError, DivisionByZeroError, VarAlreadyDefined, FileError {
        IDict<String, IValue> symTable= state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.isDefined(varName))
            throw new VarNotDefinedError(String.format("Variable %s not declared",varName));
        IValue value= symTable.lookup(varName);
        if (!(value.getType() instanceof RefType))
            throw new InvalidTypeError(String.format("Variable %s is not of ref type",varName));
        RefValue refValue=(RefValue)value;
        IValue eval=exp.eval(symTable,heap );
        IType locType= value.getLocation();
        if (!locType.equals(eval.getType()))
            throw new InvalidTypeError(String.format("%s is not of type %s",varName,eval.getType()));
        heap.update(refValue.getAddr(),eval);
        return state;
    }

    @Override
    public String toString() {
        return String.format("rH(%s,%s)",varName,exp);
    }
}
